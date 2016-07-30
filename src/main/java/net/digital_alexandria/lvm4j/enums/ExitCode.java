package net.digital_alexandria.lvm4j.enums;

/**
 * Enum to store exit codes
 *
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public enum ExitCode
{
    // everything is bad
    EXIT_ERROR(-1),
    // all is well
    EXIT_SUCCESS(0);

    // exit code
    private final int _CODE;

    ExitCode(int code) { this._CODE = code; }

    public int code()
    {
        return _CODE;
    }
}
