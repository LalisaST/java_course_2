package edu.java.repositories.interfaces;

import edu.java.model.Link;
import java.net.URI;
import java.util.List;

public interface LinkDao {
    Link add(URI url);

    void remove(Long linkId);

    List<Link> findAll();
}
