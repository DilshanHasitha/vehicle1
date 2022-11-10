import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BannersComponent } from '../list/banners.component';
import { BannersDetailComponent } from '../detail/banners-detail.component';
import { BannersUpdateComponent } from '../update/banners-update.component';
import { BannersRoutingResolveService } from './banners-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const bannersRoute: Routes = [
  {
    path: '',
    component: BannersComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BannersDetailComponent,
    resolve: {
      banners: BannersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BannersUpdateComponent,
    resolve: {
      banners: BannersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BannersUpdateComponent,
    resolve: {
      banners: BannersRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(bannersRoute)],
  exports: [RouterModule],
})
export class BannersRoutingModule {}
