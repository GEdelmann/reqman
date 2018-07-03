/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReqmanTestModule } from '../../../test.module';
import { RMCategoryDeleteDialogComponent } from 'app/entities/rm-category/rm-category-delete-dialog.component';
import { RMCategoryService } from 'app/entities/rm-category/rm-category.service';

describe('Component Tests', () => {
    describe('RMCategory Management Delete Component', () => {
        let comp: RMCategoryDeleteDialogComponent;
        let fixture: ComponentFixture<RMCategoryDeleteDialogComponent>;
        let service: RMCategoryService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMCategoryDeleteDialogComponent]
            })
                .overrideTemplate(RMCategoryDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMCategoryDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMCategoryService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
