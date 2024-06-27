import tkinter as tk
from PIL import Image, ImageTk

# Ventana de agregar productos
ventana = tk.Tk()
ventana.title("Agregar producto")
ventana.attributes('-fullscreen', True)

#Funcion agregar producto
def agregar_producto():
    pass

# Imagen de fondo
ima_f = "fondo.png"
fondo = Image.open(ima_f)

fondo_i = ImageTk.PhotoImage(fondo)
fondo_label = tk.Label(ventana, image=fondo_i)
fondo_label.place(x=0, y=0, relwidth=1, relheight=1)

# Imagen de logo
logo_path = "logo.png"
logo_image = Image.open(logo_path)
logo_image = logo_image.resize((120, 120))
logo = ImageTk.PhotoImage(logo_image)
logo_label = tk.Label(ventana, image=logo, bg='white')
logo_label.grid(row=0, column=0, padx=30, pady=30, rowspan=2, sticky='nw')

# Frame central para centrar los widgets
frame_central = tk.Frame(ventana, bg='#5D5959')
frame_central.place(relx=0.5, rely=0.5, anchor='center')

# Título
titulo_label = tk.Label(frame_central, text="Ingresar producto", font=("Helvetica", 24), bg='white')
titulo_label.grid(row=0, column=0, columnspan=2, pady=20)

# Casillas de texto
entrylb1 = tk.Label(frame_central, text="Tipo", font=("Helvetica", 16))
entrylb1.grid(row=1, column=0, pady=10, padx=10)
entry1 = tk.Entry(frame_central, font=("Helvetica", 16))
entry1.grid(row=2, column=0, pady=10, padx=10)

entrylb2 = tk.Label(frame_central, text="Talla", font=("Helvetica", 16))
entrylb2.grid(row=3, column=0, pady=10, padx=10)
entry2 = tk.Entry(frame_central, font=("Helvetica", 16))
entry2.grid(row=4, column=0, pady=10, padx=10)

entrylb3 = tk.Label(frame_central, text="Modelo", font=("Helvetica", 16))
entrylb3.grid(row=5, column=0, pady=10, padx=10)
entry3 = tk.Entry(frame_central, font=("Helvetica", 16))
entry3.grid(row=6, column=0, pady=10, padx=10)

entrylb4 = tk.Label(frame_central, text="Precio", font=("Helvetica", 16))
entrylb4.grid(row=7, column=0, pady=10, padx=10)
entry4 = tk.Entry(frame_central, font=("Helvetica", 16))
entry4.grid(row=8, column=0, pady=10, padx=10)

# Botón Enviar
enviar_button = tk.Button(frame_central, text="Enviar", font=("Helvetica", 16))
enviar_button.grid(row=9, column=0, pady=20)

# Botón Cerrar en la esquina superior derecha
cerrar_button = tk.Button(ventana, text="Cerrar", font=("Helvetica", 12))
cerrar_button.place(relx=1.0, rely=0.0, anchor='ne', x=-10, y=10)

# Función para cerrar la ventana con la tecla Escape
def cerrar_ventana(event):
    ventana.destroy()

# Vincular la tecla Escape para cerrar la ventana
ventana.bind('<Escape>', cerrar_ventana)

ventana.mainloop()

#


