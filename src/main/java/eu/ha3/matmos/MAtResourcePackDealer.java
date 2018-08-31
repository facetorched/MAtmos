package eu.ha3.matmos;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import net.minecraft.util.ResourceLocation;

public class MAtResourcePackDealer {
    private final ResourceLocation mat_pack = new ResourceLocation("matmos", "mat_pack.json");
    private final ResourceLocation expansions = new ResourceLocation("matmos", "expansions.json");

    public List<ResourcePackRepository.Entry> findResourcePacks() {
        List<ResourcePackRepository.Entry> repo = Minecraft.getMinecraft().getResourcePackRepository().getRepositoryEntries();

        List<ResourcePackRepository.Entry> foundEntries = new ArrayList<>();

        for (ResourcePackRepository.Entry pack : repo) {
            if (checkCompatible(pack)) {
                foundEntries.add(pack);
            }
        }
        return foundEntries;
    }

    public List<ResourcePackRepository.Entry> findDisabledResourcePacks() {
        ResourcePackRepository rrr = Minecraft.getMinecraft().getResourcePackRepository();

        List<ResourcePackRepository.Entry> repo = new ArrayList<>(rrr.getRepositoryEntriesAll());
        repo.removeAll(rrr.getRepositoryEntries());

        List<ResourcePackRepository.Entry> foundEntries = new ArrayList<>();
        for (ResourcePackRepository.Entry pack : repo) {
            if (checkCompatible(pack)) {
                foundEntries.add(pack);
            }
        }
        return foundEntries;
    }

    private boolean checkCompatible(ResourcePackRepository.Entry pack) {
        return pack.getResourcePack().resourceExists(mat_pack);
    }

    public InputStream openExpansionsPointerFile(IResourcePack pack) throws IOException {
        InputStream is = pack.getInputStream(expansions);
        return is;
    }
}
