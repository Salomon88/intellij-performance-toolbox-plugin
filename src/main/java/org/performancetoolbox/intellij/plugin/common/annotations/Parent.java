package org.performancetoolbox.intellij.plugin.common.annotations;

import org.performancetoolbox.intellij.plugin.common.settings.HistorySettings;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Parent {

    Class<? extends HistorySettings>[] children();

}
