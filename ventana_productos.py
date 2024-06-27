import tkinter as tk
import tkinapp
from tkinter import ttk
from PIL import Image, ImageTk
import requests

# Ventana principal
ventana = tk.Tk()
ventana.title("Tabla Inventario de Productos")
ventana.geometry("800x600")
ventana.attributes('-fullscreen', True)

ventana.configure(bg='black')
 #Imagen de fondo
imagen_fondo = "fondo.png"
fondo = Image.open(imagen_fondo) 

fondo_ima = ImageTk.PhotoImage(fondo)
fondo_label = tk.Label(ventana,image=fondo_ima)
fondo_label.place(x=0, y=0, relwidth=1, relheight=1)

logo_path = "logo.png"
logo_image = Image.open(logo_path)
logo_image = logo_image.resize((70, 70))
logo = ImageTk.PhotoImage(logo_image)
logo_label = tk.Label(ventana, image=logo, bg='white')  
logo_label.grid(row=0, column=0, padx=20, pady=2, rowspan=2, sticky='nw')

titulo_productos = tk.Label(ventana, text="Productos", font=("Helvetica", 20, "bold"), bg='black', fg='#FFD700')
titulo_productos.grid(row=0, column=2, columnspan=2, padx=10, pady=10)

# Funcion para abrir la ventana de ventas
def abrir_ventas():
    ventana.withdraw()
    tkinapp.ventana_ventas(ventana)

ventas_btn = tk.Button(ventana, text="Tabla ventas", command=abrir_ventas, font=("Helvetica", 14), bg='light grey', fg='black', relief='ridge', borderwidth=8)
ventas_btn.grid(row=0, column=3, padx=10, pady=10, sticky='ne')

# Tabla de productos
columns = ("ID QR", "Tipo", "Talla", "Precio", "Modelo")
tabla = ttk.Treeview(ventana, columns=columns, show="headings")

# Estilo para la tabla
style = ttk.Style()
style.theme_use('clam')
style.configure("Treeview", font=("Georgia", 12), background="white", foreground="black", fieldbackground="white")
style.configure("Treeview.Heading", font=("Georgia", 14, "bold"), foreground="black")
style.configure("TButton", font=("Georgia", 12), padding=5, background='black', foreground='black')
style.configure("TLabel", background="black", foreground="black")

for col in columns:
    tabla.heading(col, text=col)
    tabla.column(col, width=100)

tabla.grid(row=1, column=1, columnspan=3, padx=10, pady=10, sticky='nsew')

def actualizar_productos():
    for fila in tabla.get_children():
        tabla.delete(fila)
    respuesta = requests.get("http://192.168.4.47:5000/productos")
    respuesta.raise_for_status()
    produc = respuesta.json()
    productos = []
    for pro in produc : 
        productos.append((pro["qr_id"],pro["name"],pro["talla"],pro["price"],pro["tipo"]))
    for producto in productos:
        tabla.insert("", tk.END, values=producto)

actualizar_productos()

btn_laterales = tk.Frame(ventana,bg='black' )
btn_laterales.grid(row=1, column=4, rowspan=4, padx=10, pady=10, sticky='n')

vender_btn = tk.Button(btn_laterales, text="Vender Producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
vender_btn.pack(pady=10)

agregar_btn = tk.Button(btn_laterales, text="Agregar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
agregar_btn.pack(pady=10)

buscar_btn = tk.Button(btn_laterales, text="Buscar Producto", font=("Helvetica", 10),bg='light grey', fg='black', relief='ridge', borderwidth=8)
buscar_btn.pack(pady=10)

def ventana_ingresar_id():
    ventana_id = tk.Toplevel()
    ventana_id.title("Ingresar ID del Producto")
    ventana_id.geometry("300x100")

    tk.Label(ventana_id, text="ID del Producto:").pack(pady=5)
    id_entry = tk.Entry(ventana_id)
    id_entry.pack(pady=5)

    tk.Button(ventana_id, text="Buscar", command=lambda: buscar_producto(id_entry.get(), ventana_id)).pack(pady=5)

def buscar_producto(qr_id, ventana_id):
    response = requests.get(f"http://192.168.4.47:5000/productos/qr/{qr_id}")
    if response.status_code == 200:
        producto = response.json()
        ventana_id.destroy()
        ventana_editar(producto)
    else:
        tk.messagebox.showerror("Error", "Producto no encontrado")

def ventana_editar(producto):
    ventana_editar = tk.Toplevel()
    ventana_editar.title("Editar Producto")
    ventana_editar.geometry("400x300")

    tk.Label(ventana_editar, text="Producto:").pack(pady=5)
    producto_entry = tk.Entry(ventana_editar)
    producto_entry.pack(pady=5)
    producto_entry.insert(0, producto['Producto'])

    tk.Label(ventana_editar, text="Precio:").pack(pady=5)
    precio_entry = tk.Entry(ventana_editar)
    precio_entry.pack(pady=5)
    precio_entry.insert(0, producto['Precio'])

    tk.Label(ventana_editar, text="Talla:").pack(pady=5)
    talla_entry = tk.Entry(ventana_editar)
    talla_entry.pack(pady=5)
    talla_entry.insert(0, producto['talla'])

    tk.Label(ventana_editar, text="Tipo:").pack(pady=5)
    tipo_entry = tk.Entry(ventana_editar)
    tipo_entry.pack(pady=5)
    tipo_entry.insert(0, producto['tipo'])

    tk.Button(ventana_editar, text="Actualizar", command=lambda: actualizar_producto(producto['_id'], producto_entry.get(), precio_entry.get(), talla_entry.get(), tipo_entry.get(), ventana_editar)).pack(pady=5)


def actualizar_producto(id, producto, precio, talla, tipo, ventana_editar):
    data = {
        "Producto": producto,
        "Precio": precio,
        "talla": talla,
        "tipo": tipo
    }
    respuesta = requests.put(f"http://192.168.4.47:5000/productos/{id}", json=data)
    if respuesta.status_code == 200:
        tk.messagebox.showinfo("Éxito", "Producto actualizado correctamente")
        ventana_editar.destroy()
        actualizar_productos()
    else:
        tk.messagebox.showerror("Error", "No se pudo actualizar el producto")        

editar_btn = tk.Button(btn_laterales, text="Editar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8, command=ventana_ingresar_id)
editar_btn.pack(pady=10)

ventana.grid_rowconfigure(1, weight=1)
ventana.grid_columnconfigure(2, weight=1)

ventana.mainloop()
