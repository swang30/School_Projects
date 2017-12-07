/**
 * Created by swang on 27/04/2017.
 */
// Doubly Linked List Implementation
// doubly Linked List from https://code.tutsplus.com/articles/data-structures-with-javascript-singly-linked-list-and-doubly-linked-list--cms-23392
function Node(value) {
    this.value = value;
    this.prev = null;
    this.next = null;
    this.index = 0;
}

function DoubleLinkedList() {
    this.head = null;
    this.tail = null;
    this.length = 0;
}

DoubleLinkedList.prototype =  {
    constructor: DoubleLinkedList
};

DoubleLinkedList.prototype.sort = function compare(a, b) {
    return a - b;
};

DoubleLinkedList.prototype.insert = function (value) {
    let node = new Node(value);
    if(this.length === 0) {
        this.head = node;
        this.tail = node;
        node.next = null;
        node.index = 1;
    } else {
        node.index = this.tail.index++;
        this.tail.next = node;
        node.prev = this.tail;
        this.tail = node;
        node.next = null;
    }
    this.length++;
};


DoubleLinkedList.prototype.addToFront = function (value) {
    let node = new Node(value);
    node.next = this.head;
    this.head = node;
};

DoubleLinkedList.prototype.insertInBetween = function (value, index1, index2) {
    let nodeToInsert = new Node(value);
    let node1 = DoubleLinkedList.prototype.searchNodeAt(index1);
    let node2 = node1.next;
    node1.next = nodeToInsert;
    nodeToInsert.prev = node1;
    nodeToInsert.next = node2;
    node2.prev = nodeToInsert;
};

DoubleLinkedList.prototype.searchNodeAt = function (position) {
    let count = 1;
    let currNode = this.head;
    while(count <= position) {
        currNode = currNode.next;
        count++;
    }
    return currNode;
};

DoubleLinkedList.prototype.isEmpty = function () {
    return (this.length===0);
};

DoubleLinkedList.prototype.toStr = function () {
    let currNode = this.head;
    let str = "";
    let index = 1;
    while(currNode !== null) {
        str += index + ": " + currNode.value + "\n";
        index++;
        currNode = currNode.next;
    }
    return str;
};