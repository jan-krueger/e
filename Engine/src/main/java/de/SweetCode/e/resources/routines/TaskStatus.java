package de.SweetCode.e.resources.routines;

public enum TaskStatus {

    /** if the task has never run or has been reset **/
    FRESH,

    /** if the task has not completed and needs to run again **/
    RUNNING,

    /** if the task returned a failure result **/
    FAILED,

    /** if the task returned a success result **/
    SUCCEEDED,

    /** if the task has been terminated by an ancestor **/
    CANCELLED

}
