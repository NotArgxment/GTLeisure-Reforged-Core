package org.gtlreforged.api.capability;

import org.gtlreforged.utils.datastructure.Int128;

public interface IInt128EnergyContainer {

    default Int128 getInt128InputPerSec() {
        return Int128.ZERO();
    }

    default Int128 getInt128OutputPerSec() {
        return Int128.ZERO();
    }

    default void addEnergyPerSec(long energy) {
    }

    default Int128 getInt128EnergyStored() {
        return Int128.ZERO();
    }

    default Int128 getInt128EnergyCapacity() {
        return Int128.ZERO();
    }

}
