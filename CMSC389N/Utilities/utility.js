/**
 * Created by swang on 27/04/2017.
 */
function Utility(utilityName, utilityDescription) {
    this.utilityName = utilityName;
    this.utilityDescription = utilityDescription;
    info = function () {
        return "utility name is " + this.utilityName + "\n" + "utility description is " + this.utilityDescription;
    }
}

Utility.prototype = {
    constructor : Utility
};

