import tkinter as tk
from tkinter import ttk, messagebox
import requests
from PIL import Image, ImageTk
from collections import Counter
import ventana_productos
class VentanaVentas(tk.Toplevel):
    def __init__(self, parent):
        super().__init__(parent)
        self.title("Ventas")
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

        

        # Botón cerrar en la esquina superior derecha
        cerrar_button = tk.Button(contenedor_principal, text="Productos", font=("Helvetica", 12), command=lambda: ventana_productos.abrir_ventana_productos(self))
        cerrar_button.place(relx=1.0, rely=0.0, anchor='ne', x=-10, y=10)

        # Vincular la tecla Escape para cerrar la ventana
        self.bind('<Escape>', self.cerrar_ventana)

    def cerrar_ventana(self, event=None):
        self.parent.update()
        self.destroy()

def abrir_ventana_ventas(parent):
    VentanaVentas(parent)

if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    abrir_ventana_ventas(root)
    root.mainloop()
