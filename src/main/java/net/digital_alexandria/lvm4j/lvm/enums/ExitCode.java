package net.digital_alexandria.lvm4j.lvm.enums;

/**
 * @author Simon Dirmeier {@literal simon.dirmeier@gmx.de}
 */
public enum ExitCode
{

    EXIT_ERROR(-1),
    EXIT_SUCCESS(0);

    private final int _CODE;
    ExitCode(int code) { this._CODE = code; }

    public int code()
    {
        return _CODE;
    }
}
