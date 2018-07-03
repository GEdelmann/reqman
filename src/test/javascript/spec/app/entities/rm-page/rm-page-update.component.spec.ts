/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMPageUpdateComponent } from 'app/entities/rm-page/rm-page-update.component';
import { RMPageService } from 'app/entities/rm-page/rm-page.service';
import { RMPage } from 'app/shared/model/rm-page.model';

describe('Component Tests', () => {
    describe('RMPage Management Update Component', () => {
        let comp: RMPageUpdateComponent;
        let fixture: ComponentFixture<RMPageUpdateComponent>;
        let service: RMPageService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMPageUpdateComponent]
            })
                .overrideTemplate(RMPageUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RMPageUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMPageService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new RMPage(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMPage = entity;
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
                    const entity = new RMPage();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.rMPage = entity;
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
