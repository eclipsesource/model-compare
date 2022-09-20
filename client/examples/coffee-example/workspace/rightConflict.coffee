<?xml version="1.0" encoding="ASCII"?>
<org.eclipse.emfcloud.coffee.model:Machine xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:org.eclipse.emfcloud.coffee.model="http://www.eclipse.org/emfcloud/coffee/model" id="_uTowcMnNEeuzYY8U8dVg6Q" name="SuperBrewer3000">
  <children xsi:type="org.eclipse.emfcloud.coffee.model:BrewingUnit" id="4edecb17-80bf-4938-949b-723468322ecf">
    <children xsi:type="org.eclipse.emfcloud.coffee.model:ControlUnit" id="5058c1f8-d292-49b5-ac24-8efba98e242a">
      <processor id="099634c6-c759-4da4-b482-902a0f85a711" vendor="Qualcommm" clockSpeed="5" numberOfCores="10" socketconnectorType="Z51" thermalDesignPower="1000"/>
      <dimension id="f2141765-95ba-426d-9fa9-2e1e14784072" width="10" height="12" length="13"/>
      <display id="b99b3974-e3da-490a-ac69-ef921c0f9707" width="10" height="20"/>
    </children>
  </children>
  <workflows id="_KjaRcMnNEeuzYY8U8dVg6Q" name="BrewingFlow">
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:AutomaticTask" id="6e30dc6e-cc62-4287-a6d4-2b25e58e427d" name="Preheat"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:ManualTask" id="8de0c429-c144-4492-938b-6bb0968eabd3" name="Refill water"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:ManualTask" id="1b5dc659-bd2f-4696-a22b-bea925cf026f" name="Drink"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:ManualTask" id="27a635ae-5a5f-4546-9a07-01aecb684bfd" name="Push"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:ManualTask" id="1c893904-02c1-4876-bdb9-a1143d1d6154" name="Check drip tray"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:AutomaticTask" id="76abc67b-61d9-4f9c-9b85-dc9ff6f34095" name="Check Water"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:AutomaticTask" id="8acc2028-975c-4f46-9147-3f576386d188" name="Water Ok"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:Decision" id="a73092cf-37e8-4bc3-a36d-5e9609c2ca2d"/>
    <nodes xsi:type="org.eclipse.emfcloud.coffee.model:Merge" id="571bf651-67ba-490c-af56-3b32ee824830"/>
    <flows id="3b43103b-cc04-45af-99d9-1940687109e9" source="27a635ae-5a5f-4546-9a07-01aecb684bfd" target="76abc67b-61d9-4f9c-9b85-dc9ff6f34095"/>
    <flows id="63f7e210-a4fe-494e-a945-31101384bcad" source="76abc67b-61d9-4f9c-9b85-dc9ff6f34095" target="a73092cf-37e8-4bc3-a36d-5e9609c2ca2d"/>
    <flows xsi:type="org.eclipse.emfcloud.coffee.model:WeightedFlow" id="f410d003-8f18-4caf-bdef-5e1478094611" source="a73092cf-37e8-4bc3-a36d-5e9609c2ca2d" target="8de0c429-c144-4492-938b-6bb0968eabd3"/>
    <flows xsi:type="org.eclipse.emfcloud.coffee.model:WeightedFlow" id="727e2f4a-69ba-47f2-b1b0-1e7b7df564a9" source="a73092cf-37e8-4bc3-a36d-5e9609c2ca2d" target="8acc2028-975c-4f46-9147-3f576386d188"/>
    <flows id="763b2a75-0694-43a0-8be3-39632f500bf6" source="8acc2028-975c-4f46-9147-3f576386d188" target="571bf651-67ba-490c-af56-3b32ee824830"/>
    <flows id="b8da661a-ac64-4cfc-9991-dfc1c7ceb315" source="571bf651-67ba-490c-af56-3b32ee824830" target="1c893904-02c1-4876-bdb9-a1143d1d6154"/>
    <flows id="2b2f20ba-df24-472b-b367-54cc6829106b" source="1c893904-02c1-4876-bdb9-a1143d1d6154" target="6e30dc6e-cc62-4287-a6d4-2b25e58e427d"/>
  </workflows>
</org.eclipse.emfcloud.coffee.model:Machine>
