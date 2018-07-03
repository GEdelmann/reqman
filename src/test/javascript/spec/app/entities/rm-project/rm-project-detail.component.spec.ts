/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMProjectDetailComponent } from 'app/entities/rm-project/rm-project-detail.component';
import { RMProject } from 'app/shared/model/rm-project.model';

describe('Component Tests', () => {
    describe('RMProject Management Detail Component', () => {
        let comp: RMProjectDetailComponent;
        let fixture: ComponentFixture<RMProjectDetailComponent>;
        const route = ({ data: of({ rMProject: new RMProject(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMProjectDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMProjectDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMProjectDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMProject).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
