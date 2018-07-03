/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { ReqmanTestModule } from '../../../test.module';
import { RMProjectDeleteDialogComponent } from 'app/entities/rm-project/rm-project-delete-dialog.component';
import { RMProjectService } from 'app/entities/rm-project/rm-project.service';

describe('Component Tests', () => {
    describe('RMProject Management Delete Component', () => {
        let comp: RMProjectDeleteDialogComponent;
        let fixture: ComponentFixture<RMProjectDeleteDialogComponent>;
        let service: RMProjectService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMProjectDeleteDialogComponent]
            })
                .overrideTemplate(RMProjectDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMProjectDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RMProjectService);
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
