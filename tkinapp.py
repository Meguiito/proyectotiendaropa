import tkinter as tk
from tkinter import ttk, messagebox
import requests

class VentanaVentas(tk.Tk):
    def __init__(self):
        super().__init__()
        
        self.title("Ventana de Ventas")
        self.geometry("600x400")
        
       
        self.tabla = ttk.Treeview(self, columns=("id_venta", "id_qr_producto", "producto", "precio"), show='headings')
        self.tabla.heading("id_venta", text="ID Venta")
        self.tabla.heading("id_qr_producto", text="ID QR Producto")
        self.tabla.heading("producto", text="Producto")
        self.tabla.heading("precio", text="Precio")
        
        self.tabla.column("id_venta", anchor=tk.CENTER, width=100)
        self.tabla.column("id_qr_producto", anchor=tk.CENTER, width=200)
        self.tabla.column("producto", anchor=tk.CENTER, width=100)
        self.tabla.column("precio", anchor=tk.CENTER, width=100)
        
        self.tabla.pack(fill=tk.BOTH, expand=True)
        
        
        self.boton_cargar_ventas = tk.Button(self, text="Cargar Ventas", command=self.cargar_ventas)
        self.boton_cargar_ventas.pack(pady=10)
        
    def cargar_ventas(self):
        try:
            response = requests.get("http://192.168.1.13:5000/ventas")
            response.raise_for_status()
            ventas = response.json()['ventas']
            
          
            for item in self.tabla.get_children():
                self.tabla.delete(item)
                
            for venta in ventas:
                self.tabla.insert("", "end", values=(venta["id_venta"], venta["qr_id"], venta["producto"], venta["precio"]))
        
        except requests.exceptions.RequestException as e:
            messagebox.showerror("Error", f"No se pudieron cargar las ventas: {e}")

if __name__ == "__main__":
    app = VentanaVentas()
    app.mainloop()
