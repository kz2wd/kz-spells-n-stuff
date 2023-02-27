package com.cludivers.kz2wdprison.framework.beans.city

enum class AreaPermissionEnum(val wildernessDefaultValue: Boolean, val nationDefaultValue: Boolean) {
    CanBreak(false, false),
    CanPlace(false, false),
    CanPVP(false, false)
}