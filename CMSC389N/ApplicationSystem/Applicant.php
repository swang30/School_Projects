<?php

class Applicant {
    var $name;
    var $email;
    var $GPA;
    var $year;
    var $gender;
    var $password;

    function __construct($name, $email, $GPA, $year, $gender, $password)
    {
        $this->name = $name;
        $this->email = $email;
        $this->GPA = $GPA;
        $this->year = $year;
        $this->gender = $gender;
        $this->password = $password;
    }

}

?>