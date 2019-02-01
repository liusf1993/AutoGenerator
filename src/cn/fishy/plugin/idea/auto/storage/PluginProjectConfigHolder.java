package cn.fishy.plugin.idea.auto.storage;

import cn.fishy.plugin.idea.auto.storage.domain.PluginProjectConfig;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StoragePathMacros;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

/**
 * User: duxing Date: 2015.8.11
 */

@com.intellij.openapi.components.State(
    name = "PluginProjectConfig",
    storages = {
        @Storage(file = StoragePathMacros.WORKSPACE_FILE),
        @Storage(file = StoragePathMacros.WORKSPACE_FILE + "/autoProjectGenerator.xml", scheme = StorageScheme.DIRECTORY_BASED)
    }
)
public class PluginProjectConfigHolder implements PersistentStateComponent<PluginProjectConfig> {

  public PluginProjectConfig pluginProjectConfig = new PluginProjectConfig();

  @Nullable
  public static PluginProjectConfig getPluginProjectConfig() {
    if (Env.project == null) {
      return null;
    }
    try {
      return ServiceManager.getService(Env.project, PluginProjectConfigHolder.class).getState();
    } catch (Exception e) {
      return null;
    }
  }

  @Nullable
  @Override
  public PluginProjectConfig getState() {
    return pluginProjectConfig;
  }

  @Override
  public void loadState(PluginProjectConfig state) {
    XmlSerializerUtil.copyBean(state, pluginProjectConfig);
  }

}
