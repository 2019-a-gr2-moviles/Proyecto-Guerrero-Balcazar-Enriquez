/**
 * Libro.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    nombreAtributo: {
      type:'number'
    },
    titulo: {
      type: 'string',
      required: true,
      minLength: 3,
      maxLength: 60,
    },
    isbn:{
      type:'string',
      required:true,
      minLength: 13,
      maxLength: 13,
    },
    autor: {
      type: 'string',
      required:true,
      minLength: 3,
      maxLength: 60,
    },
    edicion: {
      type: 'number',
      required: true,
     

    },
    editorial: {
      type: 'string',
      required:true,

    },
    precio: {
      type: 'number',
      required: true
    },
    estado: {
      type: 'string',
      required: true,
      enum: ['disponible', 'no disponible']
    },
    idioma: {
      type: 'string',
      required: true,
    },
    categoriaRelacionLibro: {
      collection: 'historiaCategoriaLibro',
      via: 'idLibro'
    },
    sedeRelacionLibro: {
      collection: 'historialSedeLibro',
      via: 'idLibro'
    },
    detalleLibros: {
      collection: 'detalle',
      via: 'idLibro'
    }
    






  },

};

