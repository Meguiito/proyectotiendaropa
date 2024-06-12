from flask import Flask, jsonify, request
from flask_cors import CORS, cross_origin
from pymongo import MongoClient
from bson import ObjectId

# Instantiation
app = Flask(__name__)
client = MongoClient("mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
db = client.bananashop
productos_collection = db.productos
ventas_collection = db.ventas

# Settings
cors = CORS(app, resources={r"/*": {"origins": "*"}})

# Routes

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
    producto = productos_collection.find_one({'_id': ObjectId(id)})
    if producto:
        return jsonify({
            '_id': str(producto['_id']),
            'name': producto['Producto'],
            'price': producto['Precio']
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
@cross_origin()
def add_venta_from_qr():
    data = request.json
    qr_id = data.get("qr_id")
    producto = data.get("producto")
    precio = data.get("precio")
    venta_data = {
        "Producto": producto,
        "Precio": precio
    }
    venta_id = ventas_collection.insert_one(venta_data).inserted_id
    return jsonify({'_id': str(venta_id)}), 201

@app.route('/ventas', methods=['POST'])
@cross_origin()
def add_venta():
    data = request.json
    qr_id = data.get("qr_id")  # Obtiene el QR ID del cuerpo de la solicitud
    producto = productos_collection.find_one({'qr_id': qr_id})
    if producto:
        venta_data = {
            "Producto": producto["Producto"],
            "Precio": producto["Precio"]
        }
        venta_id = ventas_collection.insert_one(venta_data).inserted_id
        return jsonify({'_id': str(venta_id)}), 201
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/productos/<id>', methods=['PUT'])
@cross_origin()
def update_producto(id):
    data = request.json
    productos_collection.update_one({'_id': ObjectId(id)}, {"$set": data})
    return jsonify({'message': 'Producto actualizado'}), 200

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
            "Producto": producto["Producto"],
            "Precio": producto["Precio"]
        }
        venta_id = ventas_collection.insert_one(venta_data).inserted_id
        return jsonify({'_id': str(venta_id)}), 201
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

@app.route('/ventas', methods=['GET'])
@cross_origin()
def get_ventas():
    page = int(request.args.get('page', 1))
    limit = int(request.args.get('limit', 15))
    skips = limit * (page - 1)
    
    ventas = []
    cursor = ventas_collection.find().skip(skips).limit(limit)
    for venta in cursor:
        ventas.append({
            '_id': str(venta['_id']),
            'name': venta['Producto'],
            'price': venta['Precio']
        })
    
    total_ventas = ventas_collection.count_documents({})
    total_pages = (total_ventas + limit - 1) // limit  # Calculo del número total de páginas
    
    return jsonify({
        'ventas': ventas,
        'total_pages': total_pages,
        'current_page': page
    })

@app.route('/ventas/<id>', methods=['GET'])
@cross_origin()
def get_venta(id):
    venta = ventas_collection.find_one({'_id': ObjectId(id)})
    if venta:
        return jsonify({
            '_id': str(venta['_id']),
            'name': venta['Producto'],
            'price': venta['Precio']
        })
    else:
        return jsonify({'error': 'Producto no encontrado'}), 404

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)