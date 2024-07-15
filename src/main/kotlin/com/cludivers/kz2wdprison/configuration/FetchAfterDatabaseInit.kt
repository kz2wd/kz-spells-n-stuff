package com.cludivers.kz2wdprison.configuration

import jakarta.persistence.Entity
import org.hibernate.SessionFactory
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.reflect.full.companionObjectInstance


@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class FetchAfterDatabaseInit {
    companion object {
        fun initializeEverything(sessionFactory: SessionFactory) {
            sessionFactory.metamodel.managedTypes
                .filter { it.javaType.isAnnotationPresent(Entity::class.java) }
                .map { it.javaType }
                .mapNotNull {
                    it.kotlin.companionObjectInstance
                }.forEach { companionInst ->
                    companionInst.javaClass.declaredMethods.filter { it.getAnnotation(FetchAfterDatabaseInit::class.java) != null }
                        .forEach { mtd ->
                            mtd.isAccessible = true
                            Logger.getGlobal().log(Level.INFO, "${mtd.name} invoked")
                            mtd.invoke(companionInst)
                        }
                }
        }
    }
}

