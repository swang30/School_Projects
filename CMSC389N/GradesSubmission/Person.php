<?php

/**
 * Created by PhpStorm.
 * User: swang
 * Date: 27/02/2017
 * Time: 12:20 AM
 */
class Person
{
    var $name;
    var $grade;

    function __construct($name, $grade)
    {
        $this->name = $name;
        $this->grade = $grade;
    }

    function __set($name, $value)
    {
        // TODO: Implement __set() method.
        $this->$name = $value;
    }

    public function getName() {
        return $this->name;
    }

    public function  getGrade() {
        return $this->grade;
    }

    function  __toString()
    {
        // TODO: Implement __toString() method.
        return "<b>Name:</b>" . $this->name;
    }
}