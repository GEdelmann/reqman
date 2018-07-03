/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMAttachementUpdateComponent } from 'app/entities/rm-attachement/rm-attachement-update.component';
import { RMAttachementService } from 'app/entities/rm-attachement/rm-attachement.service';
import { RMAttachement } from 'app/shared/model/rm-attachement.model';

describe('Component Tests', () => {
    describe('RMAttachement Management Update Component', () => {
        let comp: RMAttachementUpdateComponent;
        let fixture: ComponentFixture<RMAttachementUpdateComponent>;
        let service: RMAttachementService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMAttachementUpdateComponent]
            })
                .overrideTemplate(RMAttachementUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMAttachementUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMAttachementService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMAttachement(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMAttachement = entity;
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
                    const entity = new RMAttachement();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMAttachement = entity;
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
