import tkinter as tk
import tkinapp
from tkinter import ttk
from PIL import Image, ImageTk

# Ventana principal
ventana = tk.Tk()
ventana.title("Tabla Inventario de Productos")
ventana.geometry("800x600")

bg_path = "fondo.png"
bg_image = Image.open(bg_path)
bg_image = bg_image.resize((800, 600)) 
bg_photo = ImageTk.PhotoImage(bg_image)

bg_label = tk.Label(ventana, image=bg_photo)
bg_label.place(x=0, y=0, relwidth=1, relheight=1)

# Cargar y configurar el logo
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

# Ejemplos de productos (de momento)
productos = [
    ("ID1", "pantalon", "L", "15000", "buzo"),
    ("ID2", "poleron", "L", "20000", ""),
    ("ID3", "camiseta", "XL", "10000", "")
]

for producto in productos:
    tabla.insert("", tk.END, values=producto)

# Botones laterales
btn_laterales = tk.Frame(ventana, bg='black')
btn_laterales.grid(row=1, column=4, rowspan=4, padx=10, pady=10, sticky='n')

vender_btn = tk.Button(btn_laterales, text="Vender Producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
vender_btn.pack(pady=10)

agregar_btn = tk.Button(btn_laterales, text="Agregar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
agregar_btn.pack(pady=10)

buscar_btn = tk.Button(btn_laterales, text="Buscar Producto", font=("Helvetica", 10),bg='light grey', fg='black', relief='ridge', borderwidth=8)
buscar_btn.pack(pady=10)

editar_btn = tk.Button(btn_laterales, text="Editar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
editar_btn.pack(pady=10)

ventana.grid_rowconfigure(1, weight=1)
ventana.grid_columnconfigure(2, weight=1)

ventana.mainloop()
