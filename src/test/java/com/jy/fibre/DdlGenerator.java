package com.jy.fibre;

import com.jy.fibre.domain.Equipment;
import com.jy.fibre.domain.TaskDetails;
import com.xmgsd.lan.gwf.core.generator.LanDdlGenerator;
import com.xmgsd.lan.gwf.core.generator.LanVueGenerator;
import com.xmgsd.lan.gwf.core.generator.MariadbDdlGenerator;
import org.junit.jupiter.api.Test;

/**
 * @author hzhou
 */
public class DdlGenerator {

    private LanDdlGenerator generator = new MariadbDdlGenerator();
    private LanVueGenerator lanVueGenerator=new LanVueGenerator();

    @Test
    public void gwf() throws Exception {
        String s = generator.generateAll("com.xmgsd.lan.gwf.domain", "com.jy.fibre.domain");
        System.out.println(s);
    }

    @Test
    public void attachment() {
        String s = generator.generateCreateTable(Equipment.class);
        System.out.println(s);
    }

    @Test
    public void generate() {
        String vue = lanVueGenerator.generate(TaskDetails.class);
        System.out.println(vue);
    }

}
