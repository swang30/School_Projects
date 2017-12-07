<?php

/**
 * Using Object-Oriented Approach as Project's requirement
 *
 */
class Student
{
    var $name;
    var $class;
    var $grades;   // grades as an array, assignment->grade

    function __construct($name, $class, $grades) {
        $this->name = $name;
        $this->class = $class;
        $this->grades = $grades;
    }

    function getName() {
        return $this->name;
    }

    function getClass() {
        return $this->class;
    }

    function getGrades() {
        return $this->grades;
    }
}