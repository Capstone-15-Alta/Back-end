package com.capstone15.alterra.domain.common.query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum FieldType {

    STRING{
        public Object parse(String value) {
            return value;
        }
    },
    INTEGER{
        public Object parse(String value){
            return Integer.valueOf(value);
        }
    },
    LONG{
        public Object parse(String value){
            return Long.valueOf(value);
        }
    },
    BOOLEAN{
        public Object parse(String value){
            return Boolean.valueOf(value);
        }
    },

    DATE{
        public Object parse(String value){
            Object date=null;
            try{
                DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                date= LocalDateTime.parse(value,dateTimeFormatter);
            }catch (Exception exception){
                throw exception;
            }
            return date;
        }
    };

    public abstract Object parse(String value);
}
