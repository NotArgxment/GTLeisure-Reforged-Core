package org.gtlcore.gtlcore.common.data.machines;

import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo;
import com.gregtechceu.gtceu.api.pattern.MultiblockShapeInfo.ShapeInfoBuilder;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Blocks;
import org.gtlcore.gtlcore.common.data.GTLMachines;
import org.gtlcore.gtlcore.utils.Registries;
import org.gtlcore.gtlcore.utils.StructureSlicer;

public class ShapeInfosModify {
   public static void init() {
      GTMachines.DISTILLATION_TOWER
         .setShapes(
            () -> {
               List<MultiblockShapeInfo> shapeInfos = new ArrayList<>();
               ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
                  .where('C', GTMachines.DISTILLATION_TOWER, Direction.NORTH)
                  .where('S', GTBlocks.CASING_STAINLESS_CLEAN.getDefaultState())
                  .where('X', GTMachines.ITEM_EXPORT_BUS[3], Direction.NORTH)
                  .where('I', GTMachines.FLUID_IMPORT_HATCH[3], Direction.NORTH)
                  .where('E', GTMachines.ENERGY_INPUT_HATCH[3], Direction.SOUTH)
                  .where('#', Blocks.f_50016_.m_49966_())
                  .where('F', GTMachines.FLUID_EXPORT_HATCH[3], Direction.SOUTH);
               List<String> front = new ArrayList<>(15);
               front.add("XCI");
               front.add("SSS");
               List<String> middle = new ArrayList<>(15);
               middle.add("SSS");
               middle.add("SSS");
               List<String> back = new ArrayList<>(15);
               back.add("SES");
               back.add("SFS");

               for (int i = 1; i <= 11; i++) {
                  front.add("SSS");
                  middle.add(1, "S#S");
                  back.add("SFS");
                  ShapeInfoBuilder copy = (ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)((ShapeInfoBuilder)builder.shallowCopy())
                           .aisle(front.toArray(String[]::new)))
                        .aisle(middle.toArray(String[]::new)))
                     .aisle(back.toArray(String[]::new));
                  shapeInfos.add(copy.build());
               }

               return shapeInfos;
            }
         );
      GTMachines.DISTILLATION_TOWER.setAllowExtendedFacing(false);
      ((MultiblockMachineDefinition)GTMachines.EVAPORATION_PLANT)
         .setShapes(
            () -> {
               ObjectArrayList<MultiblockShapeInfo> shapeInfos = new ObjectArrayList();
               ShapeInfoBuilder builder = MultiblockShapeInfo.builder()
                  .where('~', GTMachines.EVAPORATION_PLANT, Direction.SOUTH)
                  .where('A', Registries.getBlock("gtceu:aluminium_frame"))
                  .where('B', GTMachines.ENERGY_INPUT_HATCH[3], Direction.SOUTH)
                  .where('F', GTMachines.ITEM_EXPORT_BUS[3], Direction.DOWN)
                  .where('E', GTMachines.FLUID_EXPORT_HATCH[3], Direction.EAST)
                  .where('C', Registries.getBlock("gtceu:stainless_evaporation_casing"))
                  .where('G', GTLMachines.ME_DUAL_HATCH_STOCK_PART_MACHINE, Direction.EAST);
               String[][] arrays = new String[][]{
                  {"ACA", "CCC", "CCC", "CCC", " C "}, {"CFG", "C C", "C C", "C C", "CCC"}, {"ABA", "C~C", "CCE", "CCE", " C "}
               };

               for (String[][] array : StructureSlicer.sliceAndInsert(arrays, 2, 5, 3, 2, 5)) {
                  ShapeInfoBuilder copy = (ShapeInfoBuilder)builder.shallowCopy();

                  for (String[] string : array) {
                     copy = (ShapeInfoBuilder)copy.aisle(string);
                  }

                  shapeInfos.add(copy.build());
               }

               return shapeInfos;
            }
         );
      ((MultiblockMachineDefinition)GTMachines.EVAPORATION_PLANT).setAllowExtendedFacing(false);
   }
}
