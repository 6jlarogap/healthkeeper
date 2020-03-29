<?php

abstract class Enum {
    private $current_val;
    
    final public function __construct( $type ) {
        $class_name = get_class( $this );
        
        $type = strtoupper( $type );
        if ( constant( "{$class_name}::{$type}" )  === NULL ) {
            throw new Enum_Exception( 'c������� '.$type.' � ������������ '.$class_name.' �� �������.' ); 
        }
        
        $this->current_val = constant( "{$class_name}::{$type}" );
    }
    
    final public function __toString() {
        return strval($this->current_val);
    }
    
    final public function toInt() {
        return intval($this->current_val);
    }
}

class Enum_Exception extends Exception {}

?>
