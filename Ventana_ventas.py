import tkinter as tk
from tkinter import ttk, messagebox
from PIL import Image, ImageTk
import ventana_productos
import requests
class VentanaVentas(tk.Toplevel):
    def __init__(self, parent):
        super().__init__(parent)
        self.title("Ventas")
        self.attributes('-fullscreen', True)
        self.parent = parent
        self.total = 0  # Variable para el total, podrías ajustarla según tu lógica de negocio

        # Fuente más grande
        font = ("Helvetica", 15)  # Cambiar tamaño según sea necesario

        # Estilo para el Treeview
        style = ttk.Style()
        style.configure("Treeview", font=font)
        style.configure("Treeview.Heading", font=font)  # Para las cabeceras

        # Imagen de fondo
        ima_f = "fondo.png"
        fondo = Image.open(ima_f)
        self.fondo_i = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(self, image=self.fondo_i)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)

        # Imagen de logo
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((120, 120))
        self.logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(self, image=self.logo, bg='white')
        logo_label.grid(row=0, column=0, padx=30, pady=30, sticky='nw')

        # Botón cerrar en la esquina superior derecha
        cerrar_button = tk.Button(self, text="Productos", font=font, command=self.abrir_ventana_productos)
        cerrar_button.place(relx=1.0, rely=0.0, anchor='ne', x=-10, y=10)

        # Etiqueta arriba de la tabla
        etiqueta_arriba = tk.Label(self, text="Ingrese qr de los productos", font=font)
        etiqueta_arriba.place(relx=0.5, rely=0.15, anchor='center')

        # Casilla en blanco y botón "Agregar producto"
        self.casilla = tk.Entry(self, font=font)
        self.casilla.place(relx=0.44, rely=0.2, anchor='center')

        boton_agregar = tk.Button(self, text="Agregar producto", font=font, command=self.agregar_producto)
        boton_agregar.place(relx=0.55, rely=0.2, anchor='center')

        # Botón "Eliminar producto"
        boton_eliminar = tk.Button(self, text="Eliminar producto", font=font, command=self.eliminar_producto)
        boton_eliminar.place(relx=0.65, rely=0.2, anchor='center')

        # Crear tabla en el centro con 4 columnas
        columns = ("#1", "#2", "#3", "#4")
        self.tree = ttk.Treeview(self, columns=columns, show="headings", height=10)
        self.tree.heading("#1", text="Producto", anchor='center')
        self.tree.heading("#2", text="Tipo", anchor='center')
        self.tree.heading("#3", text="Talla", anchor='center')
        self.tree.heading("#4", text="Precio", anchor='center')
        
        # Configurar el tamaño de las columnas y hacer que las filas se ajusten automáticamente
        self.tree.column("#1", width=100, stretch=True)
        self.tree.column("#2", width=100, stretch=True)
        self.tree.column("#3", width=100, stretch=True)
        self.tree.column("#4", width=100, stretch=True)
        
        # Aplicar el mismo estilo de fuente a todas las columnas
        for col in columns:
            self.tree.heading(col, text=self.tree.heading(col)['text'], anchor='center')

        # Añadir datos de ejemplo a la tabla
        for i in range(1, 15):
            self.tree.insert("", "end", values=(f"Dato {i}", f"Dato {i}", f"Dato {i}", f"Dato {i}"))

        # Posicionar la tabla en el centro de la ventana
        self.tree.place(relx=0.5, rely=0.5, anchor='center', relheight=0.5, relwidth=0.5)

        # Etiqueta debajo de la tabla con el total
        etiqueta_abajo = tk.Label(self, text=f'Total : {self.total}', font=font)
        etiqueta_abajo.place(relx=0.5, rely=0.7, anchor='center')

        # Vincular la tecla Escape para cerrar la ventana
        self.bind('<Escape>', self.cerrar_ventana)

    def cerrar_ventana(self, event=None):
        self.parent.update()
        self.destroy()

    def abrir_ventana_productos(self):
        ventana_productos.abrir_ventana_productos(self)

    def agregar_producto(self):
        pass
    def eliminar_producto(self):
        # Obtener la selección actual
        seleccion = self.tree.selection()

        if not seleccion:
            messagebox.showinfo("Mensaje", "Seleccione un producto de la tabla.")
        else:
            for item in seleccion:
                self.tree.delete(item)
def abrir_ventana_ventas(ventana):
    VentanaVentas(ventana)

if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    VentanaVentas(root)
    root.mainloop()
