package com.example.common.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Link;

@XmlTransient
public class ExtendedResourceSupport extends org.springframework.hateoas.ResourceSupport {
    @XmlElement(name = "_xlink", namespace = Link.ATOM_NAMESPACE)
    @JsonProperty("_xlinks")
    private final List<ExtendedLink> _xlinks;

    public ExtendedResourceSupport() {
        super();
        this._xlinks = new ArrayList<>();
    }

    public void add(Link link) {
        if (link instanceof ExtendedLink)
            this._xlinks.add((ExtendedLink) link);
        else
            super.add(link);
    }

    public List<ExtendedLink> get_xlinks() {
        return Collections.unmodifiableList(_xlinks);
    }

    public void remove_xlinks() {
        _xlinks.clear();
    }

    public ExtendedLink get_xlink(String rel) {

        for (ExtendedLink link : _xlinks) {
            if (link.get_rel().equals(rel)) {
                return link;
            }
        }

        return null;
    }
}

