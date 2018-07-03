/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMRequirementUpdateComponent } from 'app/entities/rm-requirement/rm-requirement-update.component';
import { RMRequirementService } from 'app/entities/rm-requirement/rm-requirement.service';
import { RMRequirement } from 'app/shared/model/rm-requirement.model';

describe('Component Tests', () => {
    describe('RMRequirement Management Update Component', () => {
        let comp: RMRequirementUpdateComponent;
        let fixture: ComponentFixture<RMRequirementUpdateComponent>;
        let service: RMRequirementService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMRequirementUpdateComponent]
            })
                .overrideTemplate(RMRequirementUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMRequirementUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMRequirementService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMRequirement(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMRequirement = entity;
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
                    const entity = new RMRequirement();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMRequirement = entity;
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
