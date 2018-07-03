/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMAttachementDetailComponent } from 'app/entities/rm-attachement/rm-attachement-detail.component';
import { RMAttachement } from 'app/shared/model/rm-attachement.model';

describe('Component Tests', () => {
    describe('RMAttachement Management Detail Component', () => {
        let comp: RMAttachementDetailComponent;
        let fixture: ComponentFixture<RMAttachementDetailComponent>;
        const route = ({ data: of({ rMAttachement: new RMAttachement(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMAttachementDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMAttachementDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMAttachementDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMAttachement).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
