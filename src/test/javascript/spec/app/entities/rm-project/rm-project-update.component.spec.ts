/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMProjectUpdateComponent } from 'app/entities/rm-project/rm-project-update.component';
import { RMProjectService } from 'app/entities/rm-project/rm-project.service';
import { RMProject } from 'app/shared/model/rm-project.model';

describe('Component Tests', () => {
    describe('RMProject Management Update Component', () => {
        let comp: RMProjectUpdateComponent;
        let fixture: ComponentFixture<RMProjectUpdateComponent>;
        let service: RMProjectService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMProjectUpdateComponent]
            })
                .overrideTemplate(RMProjectUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMProjectUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMProjectService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMProject(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMProject = entity;
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
                    const entity = new RMProject();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMProject = entity;
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
