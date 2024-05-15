import random
import string
import qrcode
from pymongo import MongoClient
from pymongo.server_api import ServerApi
from PIL import ImageDraw, ImageFont

# Conexión segura a MongoDB Atlas
uri = "mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/QR?retryWrites=true&w=majority&appName=Cluster0"

client = MongoClient(uri, server_api=ServerApi("1"))

# Verificar conexión
try:
    client.admin.command("ping")
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print("Error de conexión:", e)

# Seleccionar la base de datos y la colección
db = client["bananashop"]
collection = db["QR"]

# Función para generar un ID corto
def generate_short_id(length=8):
    chars = string.ascii_letters + string.digits
    return "".join(random.choice(chars) for _ in range(length))

# Función para generar el código QR y agregar texto
def generar_qr(contenido, filename, texto):
    qr = qrcode.QRCode(version=1, box_size=10, border=5)
    qr.add_data(contenido)
    qr.make(fit=True)

    # Crear la imagen QR
    img = qr.make_image(fill_color="black", back_color="white")

    # Convertir la imagen a color para permitir la inserción de texto
    img = img.convert("RGB")
    draw = ImageDraw.Draw(img)

    # Intentar cargar la fuente
    try:
        font_path = "C:/Windows/Fonts/Arial.ttf"  # Usa una fuente común en Windows
        font = ImageFont.truetype(font_path, 16)  # Ajusta el tamaño según sea necesario
    except OSError:
        print("Error al cargar la fuente. Usando una fuente predeterminada.")
        font = ImageFont.load_default()  # Usa la fuente predeterminada si falla

    # Posicionar el texto en la parte inferior
    text_width, text_height = draw.textsize(texto, font=font)
    img_width, img_height = img.size
    text_position = ((img_width - text_width) / 2, img_height - text_height - 10)  # Colocar el texto cerca del borde inferior

    # Añadir el ID como texto
    draw.text(text_position, texto, font=font, fill="black")

    # Guardar la imagen QR con el texto
    img.save(filename)

    print(f"QR guardado como {filename}")

# Función para insertar datos en MongoDB
def insertar_en_mongo(id_producto, filename):
    documento = {
        "id_producto": id_producto,
        "qr_filename": filename,
    }
    collection.insert_one(documento)
    print("Documento insertado en la base de datos")

# Generar un ID corto y un código QR
id_producto = generate_short_id()  # ID corto de 8 caracteres
qr_filename = f"qr_{id_producto}.png"
contenido_qr = f"ID del producto: {id_producto}"

# Generar el código QR con el ID como texto
generar_qr(contenido_qr, qr_filename, texto=id_producto)

# Insertar datos en MongoDB
insertar_en_mongo(id_producto, qr_filename)
