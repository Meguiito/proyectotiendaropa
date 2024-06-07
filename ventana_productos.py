import tkinter as tk
import tkinapp
from tkinter import ttk
from PIL import Image, ImageTk

# Ventana principal
ventana = tk.Tk()
ventana.title("Tabla Inventario de Productos")
ventana.geometry("800x400")

logo_path = "logo.png"
image = Image.open(logo_path)
image = image.resize((70, 70))
logo = ImageTk.PhotoImage(image)
logo_label = tk.Label(ventana, image=logo)
logo_label.grid(row=0, column=0, padx=20, pady=2, rowspan=2, sticky='nw')

titulo_productos = tk.Label(ventana, text="Productos", font=("Helvetica", 16))
titulo_productos.grid(row=0, column=2, columnspan=2, padx=10, pady=10)

# Funcion para abrir la ventana de ventas
def abrir_ventas():
    ventana.withdraw()  
    tkinapp.ventana_ventas(ventana)

ventas_btn = tk.Button(ventana, text="Tabla ventas", command=abrir_ventas)
ventas_btn.grid(row=0, column=3, padx=10, pady=10, sticky='ne')

# tabla de productos
columns = ("ID QR", "Tipo", "Talla", "Precio", "Modelo")
tabla = ttk.Treeview(ventana, columns=columns, show="headings")

for col in columns:
    tabla.heading(col, text=col)
    tabla.column(col, width=100)

tabla.grid(row=1, column=1, columnspan=3, padx=10, pady=10, sticky='nsew')

# ejemplos de productos (de momento)
productos = [
    ("ID1", "pantalon", "L", "15000", "buzo"),
    ("ID2", "poleron", "L", "20000", ""),
    ("ID3", "camiseta", "XL", "10000", "")
]

for producto in productos:
    tabla.insert("", tk.END, values=producto)

btn_laterales = tk.Frame(ventana)
btn_laterales.grid(row=1, column=4, rowspan=4, padx=10, pady=10, sticky='n')

# Botones laterales
vender_btn = tk.Button(btn_laterales, text="Vender Producto")
vender_btn.pack(pady=10)

agregar_btn = tk.Button(btn_laterales, text="Agregar producto")
agregar_btn.pack(pady=10)

buscar_btn = tk.Button(btn_laterales, text="Buscar Producto")
buscar_btn.pack(pady=10)

editar_btn = tk.Button(btn_laterales, text="Editar producto")
editar_btn.pack(pady=10)

ventana.grid_rowconfigure(1, weight=1)
ventana.grid_columnconfigure(2, weight=1)

ventana.mainloop()
