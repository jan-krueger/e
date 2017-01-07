package de.SweetCode.e.utils.ToString;

public interface ToStringCallback {

    /**
     * Will be called to generate the string that should be appended.
     *
     * @return Gives the string that should be appended.
     */
    String create();

}
