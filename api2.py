import random
from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin
from pymongo import MongoClient
from bson import ObjectId
from bson.errors import InvalidId

# Instantiation
app = Flask(__name__)
client = MongoClient("mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
db = client.bananashop
productos_collection = db.productos
ventas_collection = db.ventas

# Settings
cors = CORS(app, resources={r"/*": {"origins": "*"}})

# Función para generar un ID de venta aleatorio
def generar_id_venta():
    return str(random.randint(100000, 999999))

# Routes

@app.route('/productos/qr/<qr_id>', methods=['GET'])
@cross_origin()
def get_producto_by_qr_id(qr_id):
    producto = productos_collection.find_one({'qr_id': qr_id})
    if producto:
        return jsonify({
            '_id': str(producto['_id']),
            'Producto': producto['Producto'],
            'Precio': producto['Precio'],
            'talla': producto['talla'],
            'tipo': producto['tipo']
        })
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/productos/<id>', methods=['PUT'])
@cross_origin()
def update_producto(id):
    data = request.json

    # Chequea si el ID es un ObjectId válido
    if len(id) == 24 and all(c in "0123456789abcdef" for c in id):
        filter_criteria = {'_id': ObjectId(id)}
    else:
        filter_criteria = {'qr_id': id}

    result = productos_collection.update_one(filter_criteria, {"$set": data})

    if result.matched_count > 0:
        return jsonify({"mensaje": "Producto actualizado correctamente"}), 200
    else:
        return jsonify({"error": "Producto no encontrado"}), 404
    
@app.route('/productoss/<id>', methods=['PUT'])
@cross_origin()
def update_productoo(id):
    data = request.json
    if '_id' in data:
        del data['_id']
    productos_collection.update_one({'_id': ObjectId(id)}, {"$set": data})
    return jsonify({'message': 'Producto actualizado'}), 200

@app.route('/agregar-producto', methods=['POST'])
@cross_origin()
def add_producto_from_qr():
    data = request.json
    qr_id = data.get("qrId")  
    producto = data.get("productName")  
    precio = data.get("productPrice")  
    talla = data.get("productSize")  
    tipo = data.get("productType") 

    existing_producto = productos_collection.find_one({"qr_id": qr_id})
    if existing_producto:
        return jsonify({'error': 'El qr_id ya existe en la base de datos'}), 400

    producto_data = {
        "Producto": producto,
        "Precio": precio,
        "qr_id": qr_id,
        "talla": talla,
        "tipo": tipo
    }

    producto_id = productos_collection.insert_one(producto_data).inserted_id
    return jsonify({'_id': str(producto_id)}), 201

@app.route('/productos', methods=['GET'])
@cross_origin()
def get_productos():
    productos = []
    for producto in productos_collection.find():
        productos.append({
            '_id': str(producto['_id']),
            'name': producto['Producto'],
            'price': producto['Precio'],
            'qr_id': producto['qr_id'],
            'talla': producto['talla'],
            'tipo': producto['tipo']
        })
    return jsonify(productos)

@app.route('/productos/<id>', methods=['GET'])
@cross_origin()
def get_producto(id):
    try:
        producto = productos_collection.find_one({'_id': ObjectId(id)})
    except InvalidId:
        return jsonify({'error': 'ID de producto no válido'}), 400

    if producto:
        return jsonify({
            '_id': str(producto['_id']),
            'name': producto['Producto'],
            'price': producto['Precio'],
            'qr_id': producto['qr_id'],
            'talla': producto['talla'],
            'tipo': producto['tipo']
        })
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/productos', methods=['POST'])
@cross_origin()
def add_producto():
    data = request.json
    producto_id = productos_collection.insert_one(data).inserted_id
    return jsonify({'_id': str(producto_id)}), 201

@app.route('/agregar-venta', methods=['POST'])
def agregar_venta():
    data = request.get_json()
    qr_id = data['qr_id']
    producto = data['producto']
    precio = data['precio']
    talla = data['talla']
    tipo = data['tipo']
    producto_encontrado = productos_collection.find_one({'qr_id': qr_id})
    if producto_encontrado:
        nueva_venta = {
            'id_venta': generar_id_venta(),
            'qr_id': qr_id,
            'producto': producto,
            'precio': precio,
            'talla': talla,
            'tipo': tipo
        }
        ventas_collection.insert_one(nueva_venta)
        return jsonify({'message': 'Venta registrada exitosamente'}), 200
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/ventas', methods=['POST'])
@cross_origin()
def add_venta():
    data = request.json
    qr_id = data.get("qr_id")
    producto = productos_collection.find_one({'qr_id': qr_id})
    if producto:
        venta_data = {
            "id_venta": generar_id_venta(),
            "Producto": producto["Producto"],
            "Precio": producto["Precio"],
            "talla": producto["talla"],
            "tipo": producto["tipo"]
        }
        venta_id = ventas_collection.insert_one(venta_data).inserted_id
        return jsonify({'_id': str(venta_id)}), 201
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/productos/<id>', methods=['DELETE'])
@cross_origin()
def delete_producto(id):
    productos_collection.delete_one({'_id': ObjectId(id)})
    return jsonify({'message': 'Producto eliminado'}), 200

@app.route('/ventas/escanear', methods=['POST'])
@cross_origin()
def escanear_qr():
    data = request.json
    qr_id = data.get("qr_id")
    producto = productos_collection.find_one({'qr_id': qr_id})
    if producto:
        venta_data = {
            "id_venta": generar_id_venta(),
            "Producto": producto["Producto"],
            "Precio": producto["Precio"],
            "talla": producto["talla"],
            "tipo": producto["tipo"]
        }
        venta_id = ventas_collection.insert_one(venta_data).inserted_id
        return jsonify({'_id': str(venta_id)}), 201
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/ventas', methods=['GET'])
@cross_origin()
def get_ventas():
    page = request.args.get('page', 1, type=int)
    per_page = 10  # Número de elementos por página
    ventas = ventas_collection.find().skip((page - 1) * per_page).limit(per_page)
    total_items = ventas_collection.count_documents({})
    total_pages = (total_items + per_page - 1) // per_page  # Cálculo del total de páginas

    ventas_data = [{
        '_id': str(venta['_id']),
        'id_venta': venta['id_venta'],
        'qr_id': venta['qr_id'],
        'producto': venta['producto'],
        'precio': venta['precio'],
        'talla': venta['talla'],
        'tipo': venta['tipo']
    } for venta in ventas]

    headers = {
        'Total-Items': total_items,
        'Total-Pages': total_pages
    }

    return jsonify(ventas_data), 200, headers

@app.route('/ventas/<id>', methods=['GET'])
@cross_origin()
def get_venta(id):
    try:
        venta = ventas_collection.find_one({'_id': ObjectId(id)})
    except InvalidId:
        return jsonify({'error': 'ID de venta no válido'}), 400

    if venta:
        return jsonify({
            '_id': str(venta['_id']),
            'id_venta': venta['id_venta'],
            'qr_id': venta['qr_id'],
            'producto': venta['producto'],
            'precio': venta['precio'],
            'talla': venta['talla'],
            'tipo': venta['tipo']
        })
    else:
        return jsonify({'error': 'Venta no encontrada'}), 404

@app.route('/ventaspc', methods=['POST'])
@cross_origin()
def add_ventapc():
    data = request.json
    print(data)
    venta_data = {
        "id_venta": generar_id_venta(),
        'id_productos':data['id_productos'],
        'total': data['total']
    }
    venta_id = ventas_collection.insert_one(venta_data).inserted_id
    return jsonify({'_id': str(venta_id)}), 201

@app.route('/ventaspc', methods=['GET'])
@cross_origin()
def get_ventas_pc():
    ventas_formateadas = []
    ventas = ventas_collection.find()

    for venta in ventas:
        id_venta = venta.get("id_venta")
        id_productos = venta.get("id_productos", [])
        total = venta.get("total", 0)
        ventas_formateadas.append({
            "id_venta": id_venta,
            "id_productos": id_productos,
            "total": total
        })
    
    return jsonify({"ventas": ventas_formateadas}), 200


if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
