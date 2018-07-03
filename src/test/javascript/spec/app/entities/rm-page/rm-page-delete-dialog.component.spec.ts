/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReqmanTestModule } from '../../../test.module';
import { RMPageDeleteDialogComponent } from 'app/entities/rm-page/rm-page-delete-dialog.component';
import { RMPageService } from 'app/entities/rm-page/rm-page.service';

describe('Component Tests', () => {
    describe('RMPage Management Delete Component', () => {
        let comp: RMPageDeleteDialogComponent;
        let fixture: ComponentFixture<RMPageDeleteDialogComponent>;
        let service: RMPageService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMPageDeleteDialogComponent]
            })
                .overrideTemplate(RMPageDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMPageDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMPageService);
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
