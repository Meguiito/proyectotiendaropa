import tkinter as tk
from tkinter import ttk, messagebox
import requests
from collections import Counter
from PIL import Image, ImageTk
import ventana_productos

class VentanaVenta(tk.Toplevel):
    def __init__(self, parent):
        super().__init__(parent)
        self.title("Ventana de Ventas")
        self.attributes('-fullscreen', True)
        self.configure(bg='black')
        self.parent = parent  # Guardar referencia a la ventana principal

        # Imagen de fondo
        imagen_fondo = "fondo.png"
        fondo = Image.open(imagen_fondo)
        fondo_ima = ImageTk.PhotoImage(fondo)
        fondo_label = tk.Label(self, image=fondo_ima)
        fondo_label.place(x=0, y=0, relwidth=1, relheight=1)
        fondo_label.image = fondo_ima

        # Cargar la imagen del logo
        logo_path = "logo.png"
        logo_image = Image.open(logo_path)
        logo_image = logo_image.resize((100, 100))
        logo = ImageTk.PhotoImage(logo_image)
        logo_label = tk.Label(self, image=logo, bg='white')
        logo_label.grid(row=0, column=0, padx=20, pady=10, rowspan=2, sticky='nw')
        logo_label.image = logo

        # Título de Ventas
        titulo_ventas = tk.Label(self, text="Ventas", font=("Helvetica", 28, "bold"), bg='black', fg='#FFD700')
        titulo_ventas.grid(row=0, column=1, columnspan=3, padx=20, pady=20, sticky='n')

        # Tabla de ventas
        columns = ("ID Venta", "ID Productos", "Total")
        self.tabla = ttk.Treeview(self, columns=columns, show="headings")

        # Estilo para la tabla
        style = ttk.Style()
        style.theme_use('clam')
        style.configure("Treeview", font=("Georgia", 12), background="white", foreground="black", fieldbackground="white")
        style.configure("Treeview.Heading", font=("Georgia", 14, "bold"), foreground="black")

        for col in columns:
            self.tabla.heading(col, text=col)
            self.tabla.column(col, width=100)

        self.tabla.grid(row=1, column=1, columnspan=3, padx=10, pady=10, sticky='nsew')

        # Botón para cargar ventas
        boton_cargar_ventas = tk.Button(self, text="Cargar Ventas", command=self.cargar_ventas, font=("Helvetica", 14), bg='light grey', fg='black', relief='ridge', borderwidth=8)
        boton_cargar_ventas.grid(row=2, column=1, sticky="w", padx=10, pady=10)

        # Botón para ver productos más vendidos
        boton_otro = tk.Button(self, text="Productos más vendidos", command=self.producto_mas_vendido, font=("Helvetica", 14), bg='light grey', fg='black', relief='ridge', borderwidth=8)
        boton_otro.grid(row=2, column=2, sticky="e", padx=10, pady=10)

        # Botón para ir a la tabla de productos
        boton_tabla_productos = tk.Button(self, text="Tabla Productos", command=lambda: ventana_productos.abrir_ventana_productos(self), font=("Helvetica", 14), bg='light grey', fg='black', relief='ridge', borderwidth=8)
        boton_tabla_productos.grid(row=2, column=3, sticky="e", padx=10, pady=10)

        # Configuración para que las filas y columnas se expandan
        self.grid_rowconfigure(1, weight=1)
        self.grid_columnconfigure(2, weight=1)

        # Manejar evento para cerrar la ventana
        self.bind('<Escape>', lambda event: self.destroy())

    def cargar_ventas(self):
        try:
            response = requests.get("http://192.168.1.4:5000/ventaspc")
            response.raise_for_status()
            ventas = response.json()['ventas']
  
            for item in self.tabla.get_children():
                self.tabla.delete(item)

            for venta in ventas:
                id_venta = venta["id_venta"]
                nombres_productos = " , ".join(map(str,nombreproductos(venta["id_productos"])))

                total = venta["total"]
                self.tabla.insert("", "end", values=(id_venta,nombres_productos, total))

        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"No se pudieron cargar las ventas: {e}")

    def producto_mas_vendido(self):
        try:
            response = requests.get("http://192.168.1.4:5000/ventas")
            response.raise_for_status()
            ventas = response.json()['ventas']
            productos = [prod['producto'] for venta in ventas for prod in venta['productos']]
            precios = [prod['precio'] for venta in ventas for prod in venta['productos']]
            contador_productos = Counter(productos)
            producto_mas_vendido, cantidad = contador_productos.most_common(1)[0]
            precio_total = sum(precios[i] for i, producto in enumerate(productos) if producto == producto_mas_vendido)
            messagebox.showinfo("Producto más vendido", f"Producto: {producto_mas_vendido}\nCantidad: {cantidad}\nPrecio Total: {precio_total}")
        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"No se pudieron cargar las ventas: {e}")
       

def ventana_ventas(parent):
    VentanaVenta(parent)
def nombreproductos(id_productos):
    nombres = []
    for k in id_productos:
        respuesta = requests.get(f"http://192.168.1.4:5000/productos/qr/{k}")
        nombres.append(respuesta.json()['Producto'])
    return nombres
if __name__ == "__main__":
    root = tk.Tk()
    root.withdraw()
    ventana_ventas(root)
    root.mainloop()
