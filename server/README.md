# Uniconet Backend (FastAPI + MySQL)

Backend listo para AWS EC2. Expone la misma API que usa la app Android (`UniconetApi.kt`).

## Instalación en EC2

```bash
# 1. Actualiza e instala Python
sudo apt update && sudo apt install -y python3-pip python3-venv python3-dev default-libmysqlclient-dev

# 2. Crea venv y deps
cd server
python3 -m venv venv && source venv/bin/activate
pip install -r requirements.txt

# 3. Configura .env
cp .env.example .env
# Edita .env con tu MySQL:
# DATABASE_URL=mysql+pymysql://appuser:TuContraseñaSegura@127.0.0.1:3306/miapp

# 4. Crea la BD
mysql -u root -p < schema.sql
# Si creaste usuario appuser, usa:
# mysql -u appuser -p miapp < schema.sql

# 5. Corre el servidor
uvicorn main:app --host 0.0.0.0 --port 8080
```

Chequeo rápido:
```bash
curl http://TU_IP:8080/api/health
curl -X POST http://TU_IP:8080/api/auth/login -H "Content-Type: application/json" -d '{"email":"sebas@gmail.com","password":"sebas"}'
```

## Endpoints base `/api/`

- `POST /api/auth/login`
- `PUT /api/auth/change-password`
- `GET /api/books` · `POST /api/books` · `DELETE /api/books/{id}`
- `GET /api/foro/anuncios` · `POST /api/foro/anuncios` · `DELETE /api/foro/anuncios/{id}`
- `GET /api/foro/anuncios/{id}/comentarios` · `POST /api/foro/anuncios/{id}/comentarios`
- `GET /api/grupos` · `POST /api/grupos` · `DELETE /api/grupos/{id}` · `PUT /api/grupos/{id}/accion`
- `POST /api/uploads` (multipart)
- `GET /api/health`

Swagger: `http://TU_IP:8080/docs`

## En Android

En `network/ApiConfig.kt` pon tu IP elástica:
```kotlin
const val ELASTIC_IP = "18.XXX.XXX.XXX"
const val BASE_URL = "http://$ELASTIC_IP:8080/api/"
```

Todo ya tiene CORS `*` y permite HTTP para pruebas.

## Producción (opcional)

- Nginx como proxy inverso a `:8080` + Certbot (Let's Encrypt) para HTTPS
- `systemd` para uvicorn como servicio
- Cambia `password_hash` a bcrypt real (passlib ya está)
