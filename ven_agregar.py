import tkinter as tk
from tkinter import messagebox, ttk
import requests
from collections import Counter
from PIL import Image, ImageTk
import ventana_productos

class VentanaAgregarProductos(tk.Toplevel):
    def __init__(self, parent):
        super().__init__(parent)
        self.title("Agregar producto")
        self.attributes('-fullscreen', True)
        self.parent = parent
        
        # Contenedor principal
        contenedor_principal = tk.Frame(self)
        contenedor_principal.pack(fill=tk.BOTH, expand=True)

        # Imagen de fondo
        ima_f = "fondo.png"
        fondo = Image.open(ima_f)
        self.fondo_i = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(contenedor_principal, image=self.fondo_i)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)

        # Imagen de logo
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((120, 120))
        self.logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(contenedor_principal, image=self.logo, bg='white')
        logo_label.grid(row=0, column=0, padx=30, pady=30, rowspan=2, sticky='nw')

        # Frame central para centrar los widgets
        frame_central = tk.Frame(contenedor_principal, bg='#5D5959')
        frame_central.place(relx=0.5, rely=0.5, anchor='center')

        # Título
        titulo_label = tk.Label(frame_central, text="Ingresar producto", font=("Helvetica", 24), bg='white')
        titulo_label.grid(row=0, column=0, columnspan=2, pady=20)

        # Casillas de texto
        entrylb1 = tk.Label(frame_central, text="Tipo", font=("Helvetica", 16))
        entrylb1.grid(row=5, column=0, pady=10, padx=10)
        self.entry1 = tk.Entry(frame_central, font=("Helvetica", 16))
        self.entry1.grid(row=6, column=0, pady=10, padx=10)

        entrylb2 = tk.Label(frame_central, text="Talla", font=("Helvetica", 16))
        entrylb2.grid(row=3, column=0, pady=10, padx=10)
        self.entry2 = tk.Entry(frame_central, font=("Helvetica", 16))
        self.entry2.grid(row=4, column=0, pady=10, padx=10)

        entrylb3 = tk.Label(frame_central, text="Producto", font=("Helvetica", 16))
        entrylb3.grid(row=1, column=0, pady=10, padx=10)
        self.entry3 = tk.Entry(frame_central, font=("Helvetica", 16))
        self.entry3.grid(row=2, column=0, pady=10, padx=10)

        entrylb4 = tk.Label(frame_central, text="Precio", font=("Helvetica", 16))
        entrylb4.grid(row=7, column=0, pady=10, padx=10)
        self.entry4 = tk.Entry(frame_central, font=("Helvetica", 16))
        self.entry4.grid(row=8, column=0, pady=10, padx=10)

        entrylb5 = tk.Label(frame_central, text="Id_Qr", font=("Helvetica", 16))
        entrylb5.grid(row=9, column=0, pady=10, padx=10)
        self.entry5 = tk.Entry(frame_central, font=("Helvetica", 16))
        self.entry5.grid(row=10, column=0, pady=10, padx=10)

        # Botón Enviar
        enviar_button = tk.Button(frame_central, text="Enviar", font=("Helvetica", 16), command=self.agregar_producto)
        enviar_button.grid(row=11, column=0, pady=10, padx=10)

        # Botón Cerrar en la esquina superior derecha
        cerrar_button = tk.Button(contenedor_principal, text="Productos", font=("Helvetica", 12), command=lambda: ventana_productos.abrir_ventana_productos(self))
        cerrar_button.place(relx=1.0, rely=0.0, anchor='ne', x=-10, y=10 )

        # Vincular la tecla Escape para cerrar la ventana
        self.bind('<Escape>', self.cerrar_ventana)


    def agregar_producto(self):
        url = "http://192.168.1.4:5000/agregar-producto"
        datos = {
            "productType": self.entry1.get(),
            "productSize": self.entry2.get(),
            "productName": self.entry3.get(),
            "productPrice": self.entry4.get(),
            "qrId": self.entry5.get()
        }
        try:
            respuesta = requests.post(url, json=datos)
            respuesta.raise_for_status()
            messagebox.showinfo("Éxito", "Producto agregado exitosamente.")
            self.entry1.delete(0, tk.END); self.entry2.delete(0, tk.END); self.entry3.delete(0, tk.END)
            self.entry4.delete(0, tk.END); self.entry5.delete(0, tk.END)
        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"Error al agregar el producto: {e}")

    def cerrar_ventana(self, event=None):
        self.parent.update()
        self.destroy()

def abrir_ventana_agregar(parent):
    VentanaAgregarProductos(parent)

if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    abrir_ventana_agregar(root)
    root.mainloop()
