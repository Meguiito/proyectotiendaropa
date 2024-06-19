import tkinapp
import tkinter as tk
from tkinter import ttk
from PIL import Image, ImageTk
import requests

def abrir_ventana_productos(ventana_principal):
    ventana_principal.withdraw() 

    # Ventana principal
    ventana = tk.Toplevel()
    ventana.title("Tabla Inventario de Productos")
    ventana.geometry("800x600")
    ventana.attributes('-fullscreen', True)

    ventana.configure(bg='black')
    # Imagen de fondo
    imagen_fondo = "fondo.png"
    fondo = Image.open(imagen_fondo)

    fondo_ima = ImageTk.PhotoImage(fondo)
    fondo_label = tk.Label(ventana, image=fondo_ima)
    fondo_label.place(x=0, y=0, relwidth=1, relheight=1)

    logo_path = "logo.png"
    logo_image = Image.open(logo_path)
    logo_image = logo_image.resize((70, 70))
    logo = ImageTk.PhotoImage(logo_image)
    logo_label = tk.Label(ventana, image=logo, bg='white')
    logo_label.grid(row=0, column=0, padx=20, pady=2, rowspan=2, sticky='nw')

    titulo_productos = tk.Label(ventana, text="Productos", font=("Helvetica", 20, "bold"), bg='black', fg='#FFD700')
    titulo_productos.grid(row=0, column=2, columnspan=2, padx=10, pady=10)

    #Funcion para abrir la ventana de ventas
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
        respuesta = requests.get("http://192.168.0.6:5000/productos")
        respuesta.raise_for_status()
        produc = respuesta.json()
        productos = []
        for pro in produc:
            productos.append((pro["qr_id"], pro["name"], pro["talla"], pro["price"], pro["tipo"]))
        for producto in productos:
            tabla.insert("", tk.END, values=producto)

    actualizar_productos()

    btn_laterales = tk.Frame(ventana, bg='black')
    btn_laterales.grid(row=1, column=4, rowspan=4, padx=10, pady=10, sticky='n')

    vender_btn = tk.Button(btn_laterales, text="Vender Producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    vender_btn.pack(pady=10)

    agregar_btn = tk.Button(btn_laterales, text="Agregar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    agregar_btn.pack(pady=10)

    buscar_btn = tk.Button(btn_laterales, text="Buscar Producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    buscar_btn.pack(pady=10)

    def ventana_ingresar_id():
        ventana_id = tk.Toplevel()
        ventana_id.title("Ingresar ID del Producto")
        ventana_id.geometry("300x100")
        
        # Imagen de fondo
        fondo = Image.open("fondo.png")
        fondo_ima = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(ventana_id, image=fondo_ima)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)
        fondo_label.image = fondo_ima
        
        tk.Label(ventana_id, text="ID del Producto:").pack(pady=5)
        id_entry = tk.Entry(ventana_id)
        id_entry.pack(pady=5)

        tk.Button(ventana_id, text="Buscar", command=lambda: buscar_producto(id_entry.get(), ventana_id)).pack(pady=5)

    def buscar_producto(qr_id, ventana_id):
        response = requests.get(f"http://192.168.0.6:5000/productos/qr/{qr_id}")
        if response.status_code == 200:
            producto = response.json()
            ventana_id.destroy()
            ventana_editar(producto)
        else:
            tk.messagebox.showerror("Error", "Producto no encontrado")

    def ventana_editar(producto):
        ventana_editar = tk.Toplevel()
        ventana_editar.title("Editar Producto")
        ventana_editar.attributes('-fullscreen', True)
        
        fondo = Image.open("fondo.png")
        fondo_ima = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(ventana_editar, image=fondo_ima)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)
        fondo_label.image = fondo_ima
        
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((120, 120))
        logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(ventana_editar, image=logo, bg='white')
        logo_label.grid(row=0, column=0, padx=30, pady=30, rowspan=2, sticky='nw')
        logo_label.image = logo
        
        frame_central = tk.Frame(ventana_editar, bg='#5D5959')
        frame_central.place(relx=0.5, rely=0.5, anchor='center')
        
        titulo_label = tk.Label(frame_central, text="Editar producto", font=("Helvetica", 24), bg='white')
        titulo_label.grid(row=0, column=0, columnspan=2, pady=20)
        
        tk.Label(frame_central, text="Producto:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=1, column=0, pady=10, padx=10)
        producto_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        producto_entry.grid(row=1, column=1, pady=10, padx=10)
        producto_entry.insert(0, producto['Producto'])

        tk.Label(frame_central, text="Precio:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=2, column=0, pady=10, padx=10)
        precio_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        precio_entry.grid(row=2, column=1, pady=10, padx=10)
        precio_entry.insert(0, producto['Precio'])

        tk.Label(frame_central, text="Talla:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=3, column=0, pady=10, padx=10)
        talla_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        talla_entry.grid(row=3, column=1, pady=10, padx=10)
        talla_entry.insert(0, producto['talla'])

        tk.Label(frame_central, text="Tipo:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=4, column=0, pady=10, padx=10)
        tipo_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        tipo_entry.grid(row=4, column=1, pady=10, padx=10)
        tipo_entry.insert(0, producto['tipo'])

        tk.Button(frame_central, text="Actualizar", font=("Helvetica", 16), command=lambda: actualizar_producto(producto['_id'], producto_entry.get(), precio_entry.get(), talla_entry.get(), tipo_entry.get(), ventana_editar)).grid(row=5, column=0, columnspan=2, pady=20)
        
        # Función para cerrar la ventana
        def cerrar_ventana(event):
            ventana_editar.destroy()
        
        # Vincular la tecla Escape para cerrar la ventana
        ventana_editar.bind('<Escape>', cerrar_ventana)
        
        ventana_editar.mainloop()

    def actualizar_producto(id, producto, precio, talla, tipo, ventana_editar):
        data = {
            "Producto": producto,
            "Precio": precio,
            "talla": talla,
            "tipo": tipo
        }
        respuesta = requests.put(f"http://192.168.0.6:5000/productos/{id}", json=data)
        if respuesta.status_code == 200:
            tk.messagebox.showinfo("Éxito", "Producto actualizado correctamente")
            ventana_editar.destroy()
            actualizar_productos()
        else:
            tk.messagebox.showerror("Error", "No se pudo actualizar el producto")

    editar_btn = tk.Button(btn_laterales, text="Editar producto", font=("Helvetica", 10), bg='light grey', fg='black', relief='ridge', borderwidth=8, command=ventana_ingresar_id)
    editar_btn.pack(pady=10)

    # Función para cerrar la ventana
    def cerrar_ventana(event):
        ventana.destroy()

    # Vincular la tecla Escape para cerrar la ventana
    ventana.bind('<Escape>', cerrar_ventana)

    ventana.grid_rowconfigure(1, weight=1)
    ventana.grid_columnconfigure(2, weight=1)
    ventana.mainloop()

if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    abrir_ventana_productos(root)
    root.mainloop()
