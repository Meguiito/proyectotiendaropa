from flask import Flask, jsonify, request, send_file
from flask_cors import CORS, cross_origin
from pymongo import MongoClient
from bson import ObjectId
import os

# Instantiation
app = Flask(__name__)
client = MongoClient("mongodb+srv://benjaz:benjaz@cluster0.qzervft.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0")
db = client.bananashop
productos_collection = db.productos

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
            'price': producto['Precio']
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

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5000)
