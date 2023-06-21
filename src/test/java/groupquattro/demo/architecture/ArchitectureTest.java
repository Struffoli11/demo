package groupquattro.demo.architecture;


import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = {"groupquattro.demo"}, importOptions = {
        ImportOption.DoNotIncludeArchives.class,
        ImportOption.DoNotIncludeTests.class,
        ImportOption.DoNotIncludeJars.class
})
public class ArchitectureTest {

    @ArchTest
    public static final ArchRule servicesClassesMayOnlyBeAccessedByAPIClassesOrOtherServiceClasses = classes().that().resideInAPackage("..services..").and().haveSimpleNameEndingWith("ServiceImpl").should().onlyBeAccessed().byClassesThat().resideInAnyPackage("..services..","..api..");

    @ArchTest
    public static final ArchRule reposClassesMayOnlyBeAccessedByServiceClasses = classes().that().resideInAPackage("..repos..").should().onlyBeAccessed().byAnyPackage("..repos..","..configuration..", "..services..");

    @ArchTest
    public static final ArchRule modelClassesMayNotBeAccessedByControllerClasses = noClasses()
            .that().resideInAPackage("..api..")
            .should().accessClassesThat().resideInAPackage("..model..");

    @ArchTest
    public static final ArchRule NoServiceClassMayAccessAnyControllerClass = noClasses().that().resideInAPackage("..services..").and().haveSimpleNameEndingWith("ServiceImpl").should().accessClassesThat().resideInAnyPackage("..api..");

    @ArchTest
    public static final ArchRule noApiClassShouldAccessRepository = noClasses().that().resideInAPackage("..api..").should().accessClassesThat().resideInAnyPackage( "..repos..");

    @ArchTest
    public static final ArchRule modelClassesShouldBePublic = classes().that().resideInAnyPackage("..model..").should().bePublic();

    @ArchTest
    public static final ArchRule serviceAndRepositoriesShouldNotBeDependentOnWebLayer = noClasses().that().resideInAPackage("..services..")
            .or().resideInAPackage("..repos..")
            .should().dependOnClassesThat().resideInAPackage("..api..").because("Services and Repositories should not depend on web layer");
}
