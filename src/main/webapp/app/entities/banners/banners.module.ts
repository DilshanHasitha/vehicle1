import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BannersComponent } from './list/banners.component';
import { BannersDetailComponent } from './detail/banners-detail.component';
import { BannersUpdateComponent } from './update/banners-update.component';
import { BannersDeleteDialogComponent } from './delete/banners-delete-dialog.component';
import { BannersRoutingModule } from './route/banners-routing.module';

@NgModule({
  imports: [SharedModule, BannersRoutingModule],
  declarations: [BannersComponent, BannersDetailComponent, BannersUpdateComponent, BannersDeleteDialogComponent],
})
export class BannersModule {}
