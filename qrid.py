import random
import string
import qrcode
from pymongo import MongoClient
from pymongo.server_api import ServerApi
from PIL import Image, ImageDraw, ImageFont
from reportlab.lib.pagesizes import letter
from reportlab.pdfgen import canvas
import os

# Conexión segura a MongoDB Atlas
uri = "mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/QR?retryWrites=true&w=majority&appName=Cluster0"

client = MongoClient(uri, server_api=ServerApi("1"))

# Verificar conexión
try:
    client.admin.command("ping")
    print("Pinged your deployment. You successfully connected to MongoDB!")
except Exception as e:
    print("Error de conexión:", e)
    client.close()
    exit()

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
        font = ImageFont.truetype(font_path, 30)  # Aumenta el tamaño de la fuente
    except OSError:
        print("Error al cargar la fuente. Usando una fuente predeterminada.")
        font = ImageFont.load_default()  # Usa la fuente predeterminada si falla

    # Posicionar el texto en la parte inferior
    text_bbox = draw.textbbox((0, 0), texto, font=font)
    text_width = text_bbox[2] - text_bbox[0]
    text_height = text_bbox[3] - text_bbox[1]
    img_width, img_height = img.size
    text_position = ((img_width - text_width) / 2, img_height - text_height - 20)  # Colocar el texto cerca del borde inferior

    # Añadir el ID como texto
    draw.text(text_position, texto, font=font, fill="black")

    # Dibujar un rectángulo alrededor del QR y el texto
    padding = 20  # Espacio extra alrededor del QR y el texto
    rectangle_top_left = (padding, padding)
    rectangle_bottom_right = (img_width - padding, img_height - padding + text_height + 20)
    
    # Redimensionar la imagen para incluir el texto dentro del rectángulo
    new_img_height = img_height + text_height + 30
    new_img = Image.new("RGB", (img_width, new_img_height), "white")
    new_img.paste(img, (0, 0))
    draw = ImageDraw.Draw(new_img)
    draw.rectangle([rectangle_top_left, rectangle_bottom_right], outline="black", width=5)

    # Volver a agregar el texto después de redimensionar
    draw.text(text_position, texto, font=font, fill="black")

    # Guardar la imagen QR con el texto y el rectángulo
    new_img.save(filename)

    print(f"QR guardado como {filename}")

# Función para insertar datos en MongoDB
def insertar_en_mongo(id_producto, filename):
    documento = {
        "id_producto": id_producto,
        "qr_filename": filename,
    }
    collection.insert_one(documento)
    print("Documento insertado en la base de datos")

# Generar PDF con múltiples códigos QR organizados en una cuadrícula
def generar_pdf_con_qrs():
    pdf_filename = "qrs.pdf"
    c = canvas.Canvas(pdf_filename, pagesize=letter)

    # Configuración de la cuadrícula
    rows = 6 # Filas
    cols = 5  # Columnas
    qr_size = 100  # Tamaño de cada QR
    margin_x = 50  # Margen izquierdo
    margin_y = 50  # Margen inferior

    # Generar QRs y agregarlos a la cuadrícula en el PDF
    for row in range(rows):
        for col in range(cols):
            id_producto = generate_short_id()  # ID corto de 8 caracteres
            qr_filename = f"qr_{id_producto}.png"
            contenido_qr = f"ID del producto: {id_producto}"

            # Generar el código QR con el ID como texto
            generar_qr(contenido_qr, qr_filename, texto=id_producto)

            # Insertar datos en MongoDB (si es necesario)
            insertar_en_mongo(id_producto, qr_filename)

            # Calcular la posición del QR en la cuadrícula
            x = margin_x + col * (qr_size + 10)
            y = letter[1] - (margin_y + (row + 1) * (qr_size + 10))  # letter[1] es la altura de la página

            # Agregar el QR al PDF
            c.drawInlineImage(qr_filename, x, y, width=qr_size, height=qr_size)

            # Eliminar el archivo QR después de agregarlo al PDF
            os.remove(qr_filename)

    c.save()
    print(f"PDF generado: {pdf_filename}")

generar_pdf_con_qrs()

# Cerrar la conexión a MongoDB
client.close()
