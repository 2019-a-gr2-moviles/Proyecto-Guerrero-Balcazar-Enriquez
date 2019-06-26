/**
 * TipoUsuario.js
 *
 * @description :: A model definition represents a database table/collection.
 * @docs        :: https://sailsjs.com/docs/concepts/models-and-orm/models
 */

module.exports = {

  attributes: {
    usuarioTipo: {
      collection:'usuario',
      via: 'idTipo'
    },
    nombre:{

    type:'string',
    required: true
    } 
  },
 

};

