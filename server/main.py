"""
Uniconet Backend - FastAPI + MySQL
Compatible con la app Android (Retrofit + OkHttp) ya preparada.

Endpoints base: /api/
Auth: Bearer JWT (POST /api/auth/login)

Instalación en EC2:
  sudo apt update && sudo apt install -y python3-pip python3-venv
  python3 -m venv venv && source venv/bin/activate
  pip install -r requirements.txt
  cp .env.example .env  # y rellena
  mysql -u root -p < schema.sql
  uvicorn main:app --host 0.0.0.0 --port 8080
"""
from datetime import datetime, timedelta
from typing import Optional
import os

from fastapi import FastAPI, Depends, HTTPException, UploadFile, File
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from sqlalchemy import create_engine, Column, Integer, String, Text, Boolean, TIMESTAMP, ForeignKey, Enum as SQLEnum
from sqlalchemy.orm import sessionmaker, declarative_base, Session, relationship
from jose import jwt
from passlib.context import CryptContext

# ── Config ─────────────────────────────────────────────────
DATABASE_URL = os.getenv("DATABASE_URL", "mysql+pymysql://appuser:TuContraseñaSegura@127.0.0.1:3306/miapp")
SECRET_KEY = os.getenv("SECRET_KEY", "cambia-esto-por-una-cadena-larga-y-secreta")
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = int(os.getenv("ACCESS_TOKEN_EXPIRE_MINUTES", "10080"))
UPLOAD_DIR = os.getenv("UPLOAD_DIR", "./uploads")
os.makedirs(UPLOAD_DIR, exist_ok=True)

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def verify_password(plain: str, hashed: str) -> bool:
    # Soporta demo en texto plano y bcrypt
    try:
        return pwd_context.verify(plain, hashed)
    except Exception:
        return plain == hashed

def hash_password(plain: str) -> str:
    return pwd_context.hash(plain)

engine = create_engine(DATABASE_URL, pool_pre_ping=True)
SessionLocal = sessionmaker(bind=engine, autocommit=False, autoflush=False)
Base = declarative_base()

# ── Modelos ────────────────────────────────────────────────
class Usuario(Base):
    __tablename__ = "usuarios"
    id = Column(Integer, primary_key=True)
    nombre = Column(String(100), nullable=False)
    correo = Column(String(150), unique=True, nullable=False)
    password_hash = Column(String(255), nullable=False)
    rol = Column(String(20), nullable=False, default="estudiante")
    created_at = Column(TIMESTAMP, default=datetime.utcnow)

class Libro(Base):
    __tablename__ = "libros"
    id = Column(Integer, primary_key=True)
    titulo = Column(String(255), nullable=False)
    autor = Column(String(150), nullable=False)
    categoria = Column(String(100), default="General")
    estado = Column(String(50), default="Disponible")
    paginas = Column(String(50), default="")
    archivo_nombre = Column(String(255), default="")
    creado_por = Column(Integer, ForeignKey("usuarios.id"), nullable=True)

class Anuncio(Base):
    __tablename__ = "anuncios"
    id = Column(Integer, primary_key=True)
    autor_id = Column(Integer, ForeignKey("usuarios.id"), nullable=True)
    autor_nombre = Column(String(100), default="Profesor · Maestro")
    fecha_label = Column(String(50), default="Hoy")
    titulo = Column(String(255), default="")
    contenido = Column(Text, nullable=False)
    destinatarios = Column(String(255), default="Todos los estudiantes de la carrera")
    archivo_nombre = Column(String(255), default="")
    borrador = Column(Boolean, default=False)

class Comentario(Base):
    __tablename__ = "comentarios_foro"
    id = Column(Integer, primary_key=True)
    anuncio_id = Column(Integer, ForeignKey("anuncios.id"), nullable=False)
    autor_id = Column(Integer, ForeignKey("usuarios.id"), nullable=True)
    autor_nombre = Column(String(100), default="Estudiante")
    texto = Column(Text, nullable=False)
    fecha_label = Column(String(50), default="Hoy")

class Grupo(Base):
    __tablename__ = "grupos_especiales"
    id = Column(Integer, primary_key=True)
    nombre = Column(String(255), nullable=False)
    integrantes = Column(String(100), default="")
    estado = Column(String(50), default="Pendiente")
    descripcion = Column(Text, default="")
    fecha = Column(String(100), default="")
    maestro_nombre = Column(String(150), default="Sin asignación registrada")
    creado_por = Column(Integer, ForeignKey("usuarios.id"), nullable=True)

# ── Schemas (DTOs como en Android) ─────────────────────────
class LoginRequest(BaseModel):
    email: str
    password: str

class LoginResponse(BaseModel):
    token: str
    nombre: str = ""
    rol: str = ""

class ChangePasswordRequest(BaseModel):
    currentPassword: str
    newPassword: str

class BookDto(BaseModel):
    id: str = ""
    titulo: str = ""
    autor: str = ""
    categoria: str = ""
    estado: str = "Disponible"
    paginas: str = ""
    archivoNombre: str = ""

    class Config:
        from_attributes = True

class UploadBookRequest(BaseModel):
    titulo: str
    autor: str
    categoria: str = "General"
    archivoNombre: str = ""

class AnuncioDto(BaseModel):
    id: str = ""
    autor: str = ""
    fecha: str = ""
    titulo: str = ""
    contenido: str = ""
    destinatarios: str = "Todos los estudiantes de la carrera"
    borrador: bool = False
    archivoNombre: str = ""

    class Config:
        from_attributes = True

class PublishAnuncioRequest(BaseModel):
    titulo: str
    contenido: str
    destinatarios: str = "Todos los estudiantes de la carrera"
    archivoNombre: str = ""

class ComentarioDto(BaseModel):
    id: str = ""
    anuncioId: str = ""
    autor: str = ""
    texto: str = ""
    fecha: str = ""

class AddComentarioRequest(BaseModel):
    autor: str
    texto: str

class GrupoDto(BaseModel):
    id: str = ""
    nombre: str = ""
    integrantes: str = ""
    estado: str = "Pendiente"
    descripcion: str = ""
    fecha: str = ""
    maestro: str = ""

class CreateGrupoRequest(BaseModel):
    nombre: str
    integrantes: str = ""
    descripcion: str = ""
    fecha: str = ""
    maestro: str = ""

class GrupoActionRequest(BaseModel):
    accion: str = ""

class ApiMessage(BaseModel):
    message: str = ""

# ── App ────────────────────────────────────────────────────
app = FastAPI(title="Uniconet API", version="1.0.0", root_path="/api")
# CORS para Android (WebView / curl). En producción limita a tu dominio.
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
    allow_credentials=True,
)

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

def create_token(data: dict) -> str:
    expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    data.update({"exp": expire})
    return jwt.encode(data, SECRET_KEY, algorithm=ALGORITHM)

def get_current_user(token: str = None):
    # Demo: sin Header Authorization por ahora (header opcional)
    # En producción: usa OAuth2PasswordBearer
    return None

# ── Auth ───────────────────────────────────────────────────
@app.post("/auth/login", response_model=LoginResponse)
def login(body: LoginRequest, db: Session = Depends(get_db)):
    user = db.query(Usuario).filter(Usuario.correo == body.email).first()
    if not user or not verify_password(body.password, user.password_hash):
        raise HTTPException(status_code=401, detail="Correo o contraseña incorrectos")
    token = create_token({"sub": user.correo, "rol": user.rol})
    return LoginResponse(token=token, nombre=user.nombre, rol=user.rol)

@app.put("/auth/change-password", response_model=ApiMessage)
def change_password(body: ChangePasswordRequest, db: Session = Depends(get_db)):
    # Demo sin auth: en producción pide token y cambia el del usuario logueado
    return ApiMessage(message="Contraseña actualizada")

# ── Libros ─────────────────────────────────────────────────
@app.get("/books", response_model=list[BookDto])
def get_books(db: Session = Depends(get_db)):
    libros = db.query(Libro).order_by(Libro.id.desc()).all()
    return [
        BookDto(id=str(l.id), titulo=l.titulo, autor=l.autor, categoria=l.categoria or "", estado=l.estado or "Disponible", paginas=l.paginas or "", archivoNombre=l.archivo_nombre or "")
        for l in libros
    ]

@app.post("/books", response_model=BookDto)
def upload_book(body: UploadBookRequest, db: Session = Depends(get_db)):
    libro = Libro(titulo=body.titulo, autor=body.autor, categoria=body.categoria, archivo_nombre=body.archivoNombre)
    db.add(libro)
    db.commit()
    db.refresh(libro)
    return BookDto(id=str(libro.id), titulo=libro.titulo, autor=libro.autor, categoria=libro.categoria or "", estado=libro.estado or "Disponible", paginas=libro.paginas or "", archivoNombre=libro.archivo_nombre or "")

@app.delete("/books/{id}", response_model=ApiMessage)
def delete_book(id: int, db: Session = Depends(get_db)):
    libro = db.query(Libro).filter(Libro.id == id).first()
    if not libro:
        raise HTTPException(404, "Libro no encontrado")
    db.delete(libro)
    db.commit()
    return ApiMessage(message="Libro eliminado")

# ── Foro ───────────────────────────────────────────────────
@app.get("/foro/anuncios", response_model=list[AnuncioDto])
def get_anuncios(db: Session = Depends(get_db)):
    anuncios = db.query(Anuncio).order_by(Anuncio.id.desc()).all()
    return [
        AnuncioDto(id=str(a.id), autor=a.autor_nombre or "", fecha=a.fecha_label or "", titulo=a.titulo or "", contenido=a.contenido or "", destinatarios=a.destinatarios or "", borrador=bool(a.borrador), archivoNombre=a.archivo_nombre or "")
        for a in anuncios
    ]

@app.post("/foro/anuncios", response_model=AnuncioDto)
def publish_anuncio(body: PublishAnuncioRequest, db: Session = Depends(get_db)):
    anuncio = Anuncio(titulo=body.titulo, contenido=body.contenido, destinatarios=body.destinatarios, archivo_nombre=body.archivoNombre)
    db.add(anuncio)
    db.commit()
    db.refresh(anuncio)
    return AnuncioDto(id=str(anuncio.id), autor=anuncio.autor_nombre or "", fecha=anuncio.fecha_label or "", titulo=anuncio.titulo or "", contenido=anuncio.contenido or "", destinatarios=anuncio.destinatarios or "", borrador=bool(anuncio.borrador), archivoNombre=anuncio.archivo_nombre or "")

@app.delete("/foro/anuncios/{id}", response_model=ApiMessage)
def delete_anuncio(id: int, db: Session = Depends(get_db)):
    anuncio = db.query(Anuncio).filter(Anuncio.id == id).first()
    if not anuncio:
        raise HTTPException(404, "Anuncio no encontrado")
    db.delete(anuncio)
    db.commit()
    return ApiMessage(message="Anuncio eliminado")

@app.get("/foro/anuncios/{id}/comentarios", response_model=list[ComentarioDto])
def get_comentarios(id: int, db: Session = Depends(get_db)):
    coms = db.query(Comentario).filter(Comentario.anuncio_id == id).all()
    return [ComentarioDto(id=str(c.id), anuncioId=str(c.anuncio_id), autor=c.autor_nombre or "", texto=c.texto or "", fecha=c.fecha_label or "") for c in coms]

@app.post("/foro/anuncios/{id}/comentarios", response_model=ComentarioDto)
def add_comentario(id: int, body: AddComentarioRequest, db: Session = Depends(get_db)):
    com = Comentario(anuncio_id=id, autor_nombre=body.autor, texto=body.texto)
    db.add(com)
    db.commit()
    db.refresh(com)
    return ComentarioDto(id=str(com.id), anuncioId=str(com.anuncio_id), autor=com.autor_nombre or "", texto=com.texto or "", fecha=com.fecha_label or "")

# ── Grupos ─────────────────────────────────────────────────
@app.get("/grupos", response_model=list[GrupoDto])
def get_grupos(db: Session = Depends(get_db)):
    grupos = db.query(Grupo).order_by(Grupo.id.desc()).all()
    return [
        GrupoDto(id=str(g.id), nombre=g.nombre or "", integrantes=g.integrantes or "", estado=g.estado or "", descripcion=g.descripcion or "", fecha=g.fecha or "", maestro=g.maestro_nombre or "")
        for g in grupos
    ]

@app.post("/grupos", response_model=GrupoDto)
def create_grupo(body: CreateGrupoRequest, db: Session = Depends(get_db)):
    grupo = Grupo(nombre=body.nombre, integrantes=body.integrantes, descripcion=body.descripcion, fecha=body.fecha, maestro_nombre=body.maestro)
    db.add(grupo)
    db.commit()
    db.refresh(grupo)
    return GrupoDto(id=str(grupo.id), nombre=grupo.nombre or "", integrantes=grupo.integrantes or "", estado=grupo.estado or "", descripcion=grupo.descripcion or "", fecha=grupo.fecha or "", maestro=grupo.maestro_nombre or "")

@app.delete("/grupos/{id}", response_model=ApiMessage)
def delete_grupo(id: int, db: Session = Depends(get_db)):
    grupo = db.query(Grupo).filter(Grupo.id == id).first()
    if not grupo:
        raise HTTPException(404, "Grupo no encontrado")
    db.delete(grupo)
    db.commit()
    return ApiMessage(message="Grupo eliminado")

@app.put("/grupos/{id}/accion", response_model=GrupoDto)
def grupo_accion(id: int, body: GrupoActionRequest, db: Session = Depends(get_db)):
    grupo = db.query(Grupo).filter(Grupo.id == id).first()
    if not grupo:
        raise HTTPException(404, "Grupo no encontrado")
    grupo.estado = "Aceptado" if body.accion.lower() == "aceptar" else ("Denegado" if body.accion.lower() == "denegar" else body.accion)
    db.commit()
    return GrupoDto(id=str(grupo.id), nombre=grupo.nombre or "", integrantes=grupo.integrantes or "", estado=grupo.estado or "", descripcion=grupo.descripcion or "", fecha=grupo.fecha or "", maestro=grupo.maestro_nombre or "")

# ── Upload archivos ────────────────────────────────────────
@app.post("/uploads")
async def upload_file(file: UploadFile = File(...)):
    name = file.filename or "archivo"
    path = os.path.join(UPLOAD_DIR, name)
    with open(path, "wb") as f:
        f.write(await file.read())
    return {"nombre": name, "ruta": path}

@app.get("/health")
def health():
    return {"status": "ok", "service": "uniconet"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=int(os.getenv("PORT", "8080")))
