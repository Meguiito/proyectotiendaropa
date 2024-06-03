import tkinter as tk
from tkinter import ttk, messagebox
import requests
from collections import Counter
from PIL import Image, ImageTk

class VentanaVentas(tk.Tk):
    def __init__(self):
        super().__init__()
        
        self.title("Ventana de Ventas")
        self.geometry("800x600")
        
        # Contenedor principal
        contenedor_principal = tk.Frame(self)
        contenedor_principal.pack(fill=tk.BOTH, expand=True)
        
        frame_superior = tk.Frame(contenedor_principal)
        frame_superior.grid(row=0, column=0, columnspan=2, sticky="nsew")
        
        self.logo = Image.open("logo.png")  
        self.logo = self.logo.resize((60, 60), Image.ANTIALIAS)
        self.logo_tk = ImageTk.PhotoImage(self.logo)
        self.logo_label = tk.Label(contenedor_principal, image=self.logo_tk)
        self.logo_label.grid(row=0, column=0, sticky="nw", padx=10, pady=10)
        self.text_label = tk.Label(frame_superior, text="Ventas", font=("Arial", 24))
        self.text_label.grid(row=0, column=1, sticky="nw", padx=350, pady=10)
        # Tabla en la fila inferior
        self.tabla = ttk.Treeview(contenedor_principal, columns=("id_venta", "id_qr_producto", "producto", "precio"), show='headings')
        self.tabla.heading("id_venta", text="ID Venta")
        self.tabla.heading("id_qr_producto", text="ID QR Producto")
        self.tabla.heading("producto", text="Producto")
        self.tabla.heading("precio", text="Precio")
        
        self.tabla.column("id_venta", anchor=tk.CENTER, width=100)
        self.tabla.column("id_qr_producto", anchor=tk.CENTER, width=200)
        self.tabla.column("producto", anchor=tk.CENTER, width=100)
        self.tabla.column("precio", anchor=tk.CENTER, width=100)
        
        self.tabla.grid(row=1, column=0, columnspan=2, sticky="nsew")
        
        # Botón cargar ventas
        self.boton_cargar_ventas = tk.Button(contenedor_principal, text="Cargar Ventas", command=self.cargar_ventas)
        self.boton_cargar_ventas.grid(row=2, column=0, sticky="w", padx=10, pady=10)
        
        # Botón Productos más vendidos
        self.boton_otro = tk.Button(contenedor_principal, text="Productos más vendidos", command=self.producto_mas_vendido)
        self.boton_otro.grid(row=2, column=1, sticky="e", padx=10, pady=10)

        #Boton Tabla productos
        self.boton_otro = tk.Button(contenedor_principal, text="Tabla Productos", command=self.Tabla_Producto)
        self.boton_otro.grid(row=0, column=0, sticky="e", padx=10, pady=10,columnspan=2)
        
        # Configurar el contenedor principal para que las filas y columnas se expandan
        contenedor_principal.grid_rowconfigure(1, weight=1)
        contenedor_principal.grid_columnconfigure(0, weight=1)

    def cargar_ventas(self):
        try:
            response = requests.get("http://192.168.0.12:5000/ventas")
            response.raise_for_status()
            ventas = response.json()['ventas']
            
            for item in self.tabla.get_children():
                self.tabla.delete(item)
                
            for venta in ventas:
                self.tabla.insert("", "end", values=(venta["id_venta"], venta["qr_id"], venta["producto"], venta["precio"]))
        
        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"No se pudieron cargar las ventas: {e}")

    def producto_mas_vendido(self):
        try:
            response = requests.get("http://192.168.0.12:5000/ventas")
            response.raise_for_status()
            ventas = response.json()['ventas']
            
            productos = [venta['producto'] for venta in ventas]
            precios = [venta['precio'] for venta in ventas]
            contador_productos = Counter(productos)
            producto_mas_vendido, cantidad = contador_productos.most_common(1)[0]
            precio_total = sum(precios[i] for i, producto in enumerate(productos) if producto == producto_mas_vendido)
            messagebox.showinfo("Producto más vendido", f"Producto: {producto_mas_vendido}\nCantidad: {cantidad}\nPrecio Total: {precio_total}")
        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"No se pudieron cargar las ventas: {e}")

    def Tabla_Producto(self):
        pass

if __name__ == "__main__":
    app = VentanaVentas()
    app.mainloop()