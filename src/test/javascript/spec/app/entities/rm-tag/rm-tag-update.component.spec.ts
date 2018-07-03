/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMTagUpdateComponent } from 'app/entities/rm-tag/rm-tag-update.component';
import { RMTagService } from 'app/entities/rm-tag/rm-tag.service';
import { RMTag } from 'app/shared/model/rm-tag.model';

describe('Component Tests', () => {
    describe('RMTag Management Update Component', () => {
        let comp: RMTagUpdateComponent;
        let fixture: ComponentFixture<RMTagUpdateComponent>;
        let service: RMTagService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMTagUpdateComponent]
            })
                .overrideTemplate(RMTagUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMTagUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMTagService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMTag(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMTag = entity;
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
                    const entity = new RMTag();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMTag = entity;
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
