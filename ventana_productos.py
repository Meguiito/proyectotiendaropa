from tkinter import messagebox
import tkinapp
import tkinter as tk
from tkinter import ttk
from PIL import Image, ImageTk
import requests
import ven_agregar
import Ventana_ventas

def abrir_ventana_productos(ventana_principal):
    ventana_principal.withdraw() 

    # Ventana principal
    ventana = tk.Toplevel()
    ventana.title("Tabla Inventario de Productos")
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
    logo_image = logo_image.resize((100, 100))
    logo = ImageTk.PhotoImage(logo_image)
    logo_label = tk.Label(ventana, image=logo, bg='white')
    logo_label.grid(row=0, column=0, padx=20, pady=10, rowspan=2, sticky='nw')

    titulo_productos = tk.Label(ventana, text="Productos", font=("Helvetica", 28, "bold"), bg='black', fg='#FFD700')
    titulo_productos.grid(row=0, column=1, columnspan=3, padx=20, pady=20, sticky='n')

    #Funcion para abrir la ventana de ventas
    def abrir_ventas():
        ventana.withdraw()
        tkinapp.ventana_ventas(ventana)

    def abrir_agregar():
        ventana.withdraw()
        ven_agregar.abrir_ventana_agregar(ventana)

    def abrir_ventas():
        ventana.withdraw()
        Ventana_ventas.abrir_ventana_ventas(ventana)

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
        respuesta = requests.get("http://192.168.1.4:5000/productos")
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

    vender_btn = tk.Button(btn_laterales, text="Vender Productos", command=abrir_ventas,font=("Helvetica", 12), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    vender_btn.pack(pady=10)

    agregar_btn = tk.Button(btn_laterales, text="Agregar producto", command=abrir_agregar,font=("Helvetica", 12), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    agregar_btn.pack(pady=10)

    buscar_btn = tk.Button(btn_laterales, text="Buscar Producto", font=("Helvetica", 12), bg='light grey', fg='black', relief='ridge', borderwidth=8)
    buscar_btn.pack(pady=10)
    
    def ventana_agregar_producto():
        # Ventana de agregar productos
        ventana = tk.Toplevel()
        ventana.title("Agregar producto")
        ventana.attributes('-fullscreen', True)

        # Función agregar producto
        def agregar_producto():
            url = "http://192.168.1.4:5000/agregar-producto"
            datos = {
                "tipo": entry1.get(),
                "talla": entry2.get(),
                "producto": entry3.get(),
                "precio": entry4.get(),
                "qr_id": entry5.get()
            }
            respuesta = requests.post(url, json=datos)
            if respuesta.status_code in [200, 201]:
                messagebox.showinfo("Éxito", "Producto agregado exitosamente.")
                ventana.destroy()
                actualizar_productos()
            else:
                messagebox.showerror("Error", f"Error al agregar el producto. Código de estado: {respuesta.status_code}")

        # Imagen de fondo
        ima_f = "fondo.png"
        fondo = Image.open(ima_f)
        fondo_i = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(ventana, image=fondo_i)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)
        fondo_label.image = fondo_i

        # Imagen de logo
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((120, 120))
        logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(ventana, image=logo, bg='white')
        logo_label.grid(row=0, column=0, padx=30, pady=30, rowspan=2, sticky='nw')
        logo_label.image = logo

        # Frame central para centrar los widgets
        frame_central = tk.Frame(ventana, bg='#5D5959')
        frame_central.place(relx=0.5, rely=0.5, anchor='center')

        # Título
        titulo_label = tk.Label(frame_central, text="Ingresar producto", font=("Helvetica", 24), bg='white')
        titulo_label.grid(row=0, column=0, columnspan=2, pady=20)

        # Casillas de texto
        entrylb1 = tk.Label(frame_central, text="Tipo", font=("Helvetica", 16))
        entrylb1.grid(row=5, column=0, pady=10, padx=10)
        entry1 = tk.Entry(frame_central, font=("Helvetica", 16))
        entry1.grid(row=6, column=0, pady=10, padx=10)

        entrylb2 = tk.Label(frame_central, text="Talla", font=("Helvetica", 16))
        entrylb2.grid(row=3, column=0, pady=10, padx=10)
        entry2 = tk.Entry(frame_central, font=("Helvetica", 16))
        entry2.grid(row=4, column=0, pady=10, padx=10)

        entrylb3 = tk.Label(frame_central, text="Producto", font=("Helvetica", 16))
        entrylb3.grid(row=1, column=0, pady=10, padx=10)
        entry3 = tk.Entry(frame_central, font=("Helvetica", 16))
        entry3.grid(row=2, column=0, pady=10, padx=10)

        entrylb4 = tk.Label(frame_central, text="Precio", font=("Helvetica", 16))
        entrylb4.grid(row=7, column=0, pady=10, padx=10)
        entry4 = tk.Entry(frame_central, font=("Helvetica", 16))
        entry4.grid(row=8, column=0, pady=10, padx=10)

        entrylb5 = tk.Label(frame_central, text="Id_Qr", font=("Helvetica", 16))
        entrylb5.grid(row=9, column=0, pady=10, padx=10)
        entry5 = tk.Entry(frame_central, font=("Helvetica", 16))
        entry5.grid(row=10, column=0, pady=10, padx=10)

        # Botón Enviar
        enviar_button = tk.Button(frame_central, text="Enviar", font=("Helvetica", 16), command=agregar_producto)
        enviar_button.grid(row=11, column=0, pady=10, padx=10)

        # Botón Cerrar en la esquina superior derecha
        cerrar_button = tk.Button(ventana, text="Cerrar", font=("Helvetica", 12), command=ventana.destroy)
        cerrar_button.place(relx=1.0, rely=0.0, anchor='ne', x=-10, y=10)

        # Función para cerrar la ventana con la tecla Escape
        def cerrar_ventana(event):
            ventana.destroy()

        # Vincular la tecla Escape para cerrar la ventana
        ventana.bind('<Escape>', cerrar_ventana)

        ventana.mainloop()


    def ver_producto(producto, ventana_principal):
        ventana_ver = tk.Toplevel()
        ventana_ver.title("Información del Producto")
        ventana_ver.attributes('-fullscreen', True)
        
        fondo = Image.open("fondo.png")
        fondo_ima = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(ventana_ver, image=fondo_ima)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)
        fondo_label.image = fondo_ima
        
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((120, 120))
        logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(ventana_ver, image=logo, bg='white')
        logo_label.grid(row=0, column=0, padx=30, pady=30, rowspan=2, sticky='nw')
        logo_label.image = logo
        
        frame_central = tk.Frame(ventana_ver, bg='#5D5959', bd=2, relief=tk.RIDGE)
        frame_central.place(relx=0.5, rely=0.5, anchor='center')
        
        titulo_label = tk.Label(frame_central, text="Información del Producto", font=("Helvetica", 24), bg='#5D5959', fg='white', pady=10)
        titulo_label.grid(row=0, column=0, columnspan=2)
        
        tk.Label(frame_central, text="Producto:", font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w', width=15).grid(row=1, column=0, pady=5, padx=5, sticky='w')
        producto_label = tk.Label(frame_central, text=producto['Producto'], font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w')
        producto_label.grid(row=1, column=1, pady=5, padx=5, sticky='w')

        tk.Label(frame_central, text="Precio:", font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w', width=15).grid(row=2, column=0, pady=5, padx=5, sticky='w')
        precio_label = tk.Label(frame_central, text=producto['Precio'], font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w')
        precio_label.grid(row=2, column=1, pady=5, padx=5, sticky='w')

        tk.Label(frame_central, text="Talla:", font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w', width=15).grid(row=3, column=0, pady=5, padx=5, sticky='w')
        talla_label = tk.Label(frame_central, text=producto['talla'], font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w')
        talla_label.grid(row=3, column=1, pady=5, padx=5, sticky='w')

        tk.Label(frame_central, text="Tipo:", font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w', width=15).grid(row=4, column=0, pady=5, padx=5, sticky='w')
        tipo_label = tk.Label(frame_central, text=producto['tipo'], font=("Helvetica", 16), bg='#5D5959', fg='white', anchor='w')
        tipo_label.grid(row=4, column=1, pady=5, padx=5, sticky='w')
        # Botón de regresar
        regresar_btn = tk.Button(frame_central, text="Regresar", font=("Helvetica", 14), bg='light grey', fg='black', relief='ridge', borderwidth=8,
                                 command=lambda: regresar(ventana_ver, ventana_principal))
        regresar_btn.grid(row=5, column=0, columnspan=2, pady=20)

    def regresar(ventana_ver, ventana_principal):
        ventana_ver.destroy()
        ventana_principal.deiconify()

    def obtener_producto_seleccionado():
        selected_item = tabla.selection()
        if selected_item:
            valores = tabla.item(selected_item)["values"]
            producto = {
                "Producto": valores[1],
                "Precio": valores[3],
                "talla": valores[2],
                "tipo": valores[4],
                "ID QR": valores[0]
            }
            ver_producto(producto, ventana)
        else:
            tk.messagebox.showwarning("Selecciona un producto primero")
    buscar_btn.config(command=obtener_producto_seleccionado)
           
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
        producto_entry.insert(0, producto[1])

        tk.Label(frame_central, text="Precio:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=2, column=0, pady=10, padx=10)
        precio_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        precio_entry.grid(row=2, column=1, pady=10, padx=10)
        precio_entry.insert(0, producto[3])

        tk.Label(frame_central, text="Talla:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=3, column=0, pady=10, padx=10)
        talla_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        talla_entry.grid(row=3, column=1, pady=10, padx=10)
        talla_entry.insert(0, producto[2])

        tk.Label(frame_central, text="Tipo:", font=("Helvetica", 16), bg='#5D5959', fg='white').grid(row=4, column=0, pady=10, padx=10)
        tipo_entry = tk.Entry(frame_central, font=("Helvetica", 16))
        tipo_entry.grid(row=4, column=1, pady=10, padx=10)
        tipo_entry.insert(0, producto[4])

        tk.Button(frame_central, text="Actualizar", font=("Helvetica", 16), command=lambda: actualizar_producto(producto[0], producto_entry.get(), 
        precio_entry.get(), talla_entry.get(), tipo_entry.get(), ventana_editar)).grid(row=5, column=0, columnspan=2, pady=20)
        
        def cerrar_ventana(event):
            ventana_editar.destroy()
        
        ventana_editar.bind('<Escape>', cerrar_ventana)
        ventana_editar.mainloop()

    def actualizar_producto(id, producto, precio, talla, tipo, ventana_editar):
        data = {
            "Producto": producto,
            "Precio": precio,
            "talla": talla,
            "tipo": tipo
        }
        respuesta = requests.put(f"http://192.168.1.4:5000/productos/{id}", json=data)
        if respuesta.status_code == 200:
            tk.messagebox.showinfo("Éxito", "Producto actualizado correctamente")
            ventana_editar.destroy()
            actualizar_productos()
        else:
            tk.messagebox.showerror("Error", "No se pudo actualizar el producto")
            
    def editar_producto_seleccionado():
        seleccion = tabla.selection()
        if seleccion:
            item = tabla.item(seleccion[0])
            producto = item['values']
            ventana_editar(producto)
        else:
            messagebox.showwarning("Advertencia", "Seleccione un producto para editar.")
    
    editar_btn = tk.Button(btn_laterales, text="Editar producto", font=("Helvetica", 12), bg='light grey', fg='black', relief='ridge', borderwidth=8, 
                       command=editar_producto_seleccionado)
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
