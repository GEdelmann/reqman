/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMCategoryUpdateComponent } from 'app/entities/rm-category/rm-category-update.component';
import { RMCategoryService } from 'app/entities/rm-category/rm-category.service';
import { RMCategory } from 'app/shared/model/rm-category.model';

describe('Component Tests', () => {
    describe('RMCategory Management Update Component', () => {
        let comp: RMCategoryUpdateComponent;
        let fixture: ComponentFixture<RMCategoryUpdateComponent>;
        let service: RMCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMCategoryUpdateComponent]
            })
                .overrideTemplate(RMCategoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMCategoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMCategoryService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMCategory(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMCategory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMCategory();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMCategory = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
